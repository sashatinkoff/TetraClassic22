package com.isidroid.b21.ui.home

import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdServiceInfo
import timber.log.Timber
import java.util.Collections
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class DiscoveryListenerImpl(
    private val nsdManager: NsdManager,
    private val listener: Listener
) : DiscoveryListener {
    private var resolveListenerBusy = AtomicBoolean(false)
    private var pendingNsdServices = ConcurrentLinkedQueue<Pair<NsdServiceInfo?, NsdManager.ResolveListener>>()
    private var resolvedNsdServices: MutableList<NsdServiceInfo> = Collections.synchronizedList(ArrayList<NsdServiceInfo>())

    override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
        Timber.i("onStartDiscoveryFailed $p0, code=$p1")
    }
    override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
        Timber.i("onStopDiscoveryFailed $p0, code=$p1")
    }

    override fun onDiscoveryStarted(p0: String?) {
        Timber.i("onDiscoveryStarted $p0")
        listener.onDiscoveryStarted(p0)
    }

    override fun onDiscoveryStopped(p0: String?) {
        listener.onDiscoveryStarted(p0)
    }

    override fun onServiceFound(service: NsdServiceInfo?) {
        // If the resolver is free, resolve the service to get all the details
        Timber.i("onServiceFound ${service?.serviceName}")


        val resolveListener = createResolveListener()


        if (resolveListenerBusy.compareAndSet(false, true)) {
            Timber.i("resolveService")
            nsdManager.resolveService(service, resolveListener)
        } else {
            // Resolver was busy. Add the service to the list of pending services
            Timber.i("add pending service ${service?.serviceName}")
            pendingNsdServices.add(Pair(service, resolveListener))
        }

//        nsdManager.resolveService(service, createResolveListener())
    }

    override fun onServiceLost(service: NsdServiceInfo?) {
        val iteratorPending = pendingNsdServices.iterator()
        while (iteratorPending.hasNext()) {
            if (iteratorPending.next().first?.serviceName == service?.serviceName) {
                iteratorPending.remove()
            }
        }

        // If the lost service was in the list of resolved services, remove it
        synchronized(resolvedNsdServices) {
            val iteratorResolved = resolvedNsdServices.iterator()
            while (iteratorResolved.hasNext()) {
                if (iteratorResolved.next().serviceName == service?.serviceName)
                    iteratorResolved.remove()
            }
        }

        // Do the rest of the processing for the lost service
        listener.onNsdServiceLost(service)
    }

    private fun createResolveListener() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(service: NsdServiceInfo?, p1: Int) {
            service ?: return
            Timber.e("onResolveFailed ${service.serviceName}, reason=$p1")
            listener.onFailed(service, p1)
            resolveNextInQueue()
        }

        override fun onServiceResolved(service: NsdServiceInfo?) {
            service ?: return
            Timber.i("onServiceResolved ${service.serviceName}")
            resolvedNsdServices.add(service)
            listener.onResolved(service)
            resolveNextInQueue()
        }
    }

    // Resolve next NSD service pending resolution
    private fun resolveNextInQueue() {
        // Get the next NSD service waiting to be resolved from the queue
        val pendingItem = pendingNsdServices.poll()
        val service = pendingItem?.first
        val resolveListener = pendingItem.second

        Timber.i("resolveNextInQueue ${service?.serviceName}")

        if (service != null) {
            // There was one. Send to be resolved.
            Timber.i("resolve service")
            nsdManager.resolveService(service, resolveListener)
        } else {
            // There was no pending service. Release the flag
            resolveListenerBusy.set(false)
        }
    }


    interface Listener {
        fun onDiscoveryStarted(regType: String?)
        fun onDiscoveryStopped(regType: String?)
        fun onFailed(service: NsdServiceInfo, code: Int)
        fun onResolved(service: NsdServiceInfo)
        fun onNsdServiceLost(service: NsdServiceInfo?)
    }
}