package com.isidroid.b21.ui.home

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


private const val SERVICE_TYPE = "_pdl-datastream._tcp."

@AndroidEntryPoint
class HomeFragment : BindFragment(R.layout.fragment_home), HomeView, AppBarListener, Adapter.Listener, DiscoveryListenerImpl.Listener {
    private val viewModel by viewModels<HomeViewModel>()
    private val binding by viewBinding<FragmentHomeBinding>()
    private val nsdManager by lazy { requireActivity().getSystemService(Context.NSD_SERVICE) as NsdManager }
    private val discoveryListener by lazy { DiscoveryListenerImpl(nsdManager, this) }
    private val adapter = Adapter(this)
    private var isDiscovering = false

    override fun onInsetChanged(start: Int, top: Int, end: Int, bottom: Int) {
        binding.root.setPadding(16.dp, top, 16.dp, bottom)
    }

    override fun createAdapter() {
        binding.recyclerView.adapter = adapter
    }

    override fun createForm() {
        binding.buttonStartDiscover.setOnClickListener {
            lifecycleScope.launch {
                startDiscovery()
                    .flowOn(Dispatchers.Default)
                    .catchTimber { }
                    .collect()
            }
        }

        binding.buttonSendMessage.setOnClickListener { viewModel.sendMessage(message = binding.inputMessage.text.toString()) }
    }

    override suspend fun onCreateViewModel() {
        viewModel.viewState.collect { state ->
            when (state) {
                is UiState.ServiceItem -> adapter.add(state.info)
                UiState.Clear -> {
                    adapter.clear()
                    binding.errorTextView.text = ""
                }

                is UiState.ResolveFailed -> {
                    val message = "${state.info.serviceName} resolve failed, error=${state.code}"

                    val text = buildString {
                        append(message)
                        append("\n")
                        append(binding.errorTextView.text.toString())
                    }

                    binding.errorTextView.text = text

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }

                null -> {}
                is UiState.SelectedService -> binding.buttonSendMessage.text = buildString { append("Send message to ${state.service.serviceName}") }
                is UiState.Error -> Toast.makeText(activity, "${state.t.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startDiscovery() = flow<Boolean> {
        if (isDiscovering)
            nsdManager.stopServiceDiscovery(discoveryListener)
        else {
            nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        }

        isDiscovering = !isDiscovering
        binding.buttonStartDiscover.text = if (isDiscovering) "Stop discovering" else "Start discovering"
    }

    override fun clickOnServiceItem(item: NsdServiceInfo) {
        viewModel.selectService(item)
    }


    // DiscoveryListenerImpl.Listener
    override fun onDiscoveryStarted(regType: String?) {
        viewModel.onDiscoveryStarted()
    }

    override fun onDiscoveryStopped(regType: String?) {}

    override fun onFailed(info: NsdServiceInfo, code: Int) {
        viewModel.resolveFailed(info, code)
    }

    override fun onResolved(info: NsdServiceInfo) {
        viewModel.add(info)
    }

    override fun onNsdServiceLost(service: NsdServiceInfo?) {
    }
}
