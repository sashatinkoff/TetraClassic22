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
    private lateinit var resolveListener: NsdManager.ResolveListener
    private var resolveListenerBusy = AtomicBoolean(false)
    private var pendingNsdServices = ConcurrentLinkedQueue<NsdServiceInfo>()
    private var resolvedNsdServices: MutableList<NsdServiceInfo> = Collections.synchronizedList(ArrayList<NsdServiceInfo>())


    override fun onStartDiscoveryFailed(p0: String?, p1: Int) {}
    override fun onStopDiscoveryFailed(p0: String?, p1: Int) {}

    override fun onDiscoveryStarted(p0: String?) {
        listener.onDiscoveryStarted(p0)
    }

    override fun onDiscoveryStopped(p0: String?) {
        listener.onDiscoveryStarted(p0)
    }

    override fun onServiceFound(service: NsdServiceInfo?) {
        Timber.i("onServiceFound ${service?.serviceName}, isLocked=")

        // If the resolver is free, resolve the service to get all the details
        if (resolveListenerBusy.compareAndSet(false, true)) {
            resolveListener = createResolveListener()
            nsdManager.resolveService(service, resolveListener)
        } else {
            // Resolver was busy. Add the service to the list of pending services
            pendingNsdServices.add(service)
        }

//        nsdManager.resolveService(service, createResolveListener())
    }

    override fun onServiceLost(service: NsdServiceInfo?) {
        var iterator = pendingNsdServices.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().serviceName == service?.serviceName)
                iterator.remove()
        }

        // If the lost service was in the list of resolved services, remove it
        synchronized(resolvedNsdServices) {
            iterator = resolvedNsdServices.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().serviceName == service?.serviceName)
                    iterator.remove()
            }
        }

        // Do the rest of the processing for the lost service
        listener.onNsdServiceLost(service)
    }

    private fun createResolveListener() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(p0: NsdServiceInfo?, p1: Int) {
            p0 ?: return
            Timber.e("onResolveFailed ${p0.serviceName}, reason=$p1")
            listener.onFailed(p0, p1)
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

        Timber.e("resolveNextInQueue")

        val nextNsdService = pendingNsdServices.poll()
        if (nextNsdService != null) {
            // There was one. Send to be resolved.
            nsdManager?.resolveService(nextNsdService, resolveListener)
        } else {
            // There was no pending service. Release the flag
            resolveListenerBusy.set(false)
        }
    }


    interface Listener {
        fun onDiscoveryStarted(regType: String?)
        fun onDiscoveryStopped(regType: String?)
        fun onFailed(info: NsdServiceInfo, code: Int)
        fun onResolved(info: NsdServiceInfo)
        fun onNsdServiceLost(service: NsdServiceInfo?)
    }
}