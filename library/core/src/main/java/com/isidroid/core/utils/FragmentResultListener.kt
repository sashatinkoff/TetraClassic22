package com.isidroid.core.utils

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

interface FragmentResultListener {
    fun startFragmentResultListeners(){}
    fun stopFragmentResultListeners(){}

    fun listen(fragmentManager: FragmentManager, viewLifeCycleOwner: LifecycleOwner, requestKey: String, onComplete: (Bundle) -> Unit) {
        fragmentManager
            .setFragmentResultListener(requestKey, viewLifeCycleOwner) { _, result -> onComplete(result) }
    }

    fun listen(fragment: Fragment, requestKey: String, onComplete: (Bundle) -> Unit) {
        listen(
            fragmentManager = fragment.requireActivity().supportFragmentManager,
            viewLifeCycleOwner = fragment.viewLifecycleOwner,
            requestKey = requestKey,
            onComplete = onComplete
        )
    }

    fun stopListen(fragmentManager: FragmentManager, vararg requestKey: String) {
        requestKey.forEach { fragmentManager.clearFragmentResultListener(it) }
    }

    fun stopListen(fragment: Fragment, vararg requestKey: String) {
        stopListen(fragment.requireActivity().supportFragmentManager, *requestKey.toList().toTypedArray())
    }

    fun setFragmentResult(
        activity: FragmentActivity,
        requestCode: String,
        bundle: Bundle = bundleOf()
    ) {
        setFragmentResult(activity.supportFragmentManager, requestCode, bundle)
    }

    fun setFragmentResult(fragmentManager: FragmentManager, requestKey: String, bundle: Bundle = bundleOf()) {
        fragmentManager.setFragmentResult(requestKey, bundle)
    }
}