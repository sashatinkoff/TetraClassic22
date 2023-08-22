package com.isidroid.b21.ui.home

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.MulticastLock
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.isidroid.b21.R
import com.isidroid.b21.databinding.FragmentHomeBinding
import com.isidroid.b21.ui.home.adapter.Adapter
import com.isidroid.b21.utils.base.BindFragment
import com.isidroid.core.ext.catchTimber
import com.isidroid.core.ext.dp
import com.isidroid.core.ui.AppBarListener
import com.isidroid.core.view.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import com.isidroid.b21.ui.home.HomeViewModel.UiState as UiState

private const val SERVICE_TYPE = "_pdl-datastream._tcp."

@AndroidEntryPoint
class HomeFragment : BindFragment(R.layout.fragment_home), HomeView, AppBarListener, Adapter.Listener, DiscoveryListenerImpl.Listener {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding<FragmentHomeBinding>()
    private val nsdManager by lazy { requireActivity().getSystemService(Context.NSD_SERVICE) as NsdManager }
    private val discoveryListener by lazy { DiscoveryListenerImpl(nsdManager, this) }
    private val adapter = Adapter(this)
    private var isDiscovering = false
    private var multicastLock: MulticastLock? = null

    override fun onInsetChanged(start: Int, top: Int, end: Int, bottom: Int) {
        binding.root.setPadding(16.dp, top, 16.dp, bottom)
    }

    override fun createAdapter() {
        binding.recyclerView.adapter = adapter
    }

    override fun createForm() {
        binding.buttonStartDiscover.setOnClickListener { startDiscovery() }
        binding.buttonSendMessage.setOnClickListener { sendMessage(binding.inputMessage.text.toString()) }
    }

    override suspend fun onCreateViewModel() {
        viewModel.viewState.collect { state ->
            when (state) {
                is UiState.ServiceItem -> onServiceFound(state.info)
                is UiState.ResolveFailed -> onResolveFailed(state.info, state.code)
                is UiState.SelectedService -> onSelectService(state.service)
                is UiState.Error -> showError(state.t)

                UiState.Clear -> onCleared()

                else -> Unit
            }
        }
    }

    /**
     * Issue found on Pixel 3a. Need to acquire multicast lock
     */
    private fun createMulticastLock() {
        val wifi = requireActivity().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        multicastLock = wifi.createMulticastLock("multicastLock").apply {
            setReferenceCounted(true)
            acquire()
        }
    }

    private fun releaseMulticastLock() {
        multicastLock?.release()
        multicastLock = null
    }

    // HomeView
    override fun startDiscovery() {
        if (isDiscovering) {
            releaseMulticastLock()
            nsdManager.stopServiceDiscovery(discoveryListener)
        } else {
            createMulticastLock()
            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }

        isDiscovering = !isDiscovering
        binding.buttonStartDiscover.text = if (isDiscovering) "Stop discovering" else "Start discovering"
    }

    override fun onServiceFound(info: NsdServiceInfo) {
        adapter.add(info)
    }

    override fun onCleared() {
        adapter.clear()
        binding.errorTextView.text = ""
    }

    override fun onResolveFailed(info: NsdServiceInfo, code: Int) {
        val message = "${info.serviceName} resolve failed, error=${code}"
        binding.errorTextView.text = buildString {
            append(message)
            append("\n")
            append(binding.errorTextView.text.toString())
        }

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSelectService(service: NsdServiceInfo) {
        binding.buttonSendMessage.text = buildString { append("Send message to ${service.serviceName}") }
    }

    override fun sendMessage(message: String) {
        viewModel.sendMessage(message = message)
    }

    // Adapter.Listener
    override fun clickOnServiceItem(item: NsdServiceInfo) {
        viewModel.selectService(item)
    }

    // DiscoveryListenerImpl.Listener
    override fun onDiscoveryStarted(regType: String?) {
        viewModel.onDiscoveryStarted()
    }

    override fun onDiscoveryStopped(regType: String?) {}

    override fun onFailed(service: NsdServiceInfo, code: Int) {
        viewModel.resolveFailed(service, code)
    }

    override fun onResolved(service: NsdServiceInfo) {
        viewModel.add(service)
    }

    override fun onNsdServiceLost(service: NsdServiceInfo?) {
        lifecycleScope.launch {
            flowOf(service)
                .flowOn(Dispatchers.Main)
                .onEach {
                    val position = adapter.list.indexOfFirst { it.serviceName == service?.serviceName }
                    adapter.remove(position)
                }.collect()
        }

    }
}
