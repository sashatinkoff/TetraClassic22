package com.isidroid.b21.utils.core

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.isidroid.b21.ext.hideSoftKeyboard
import com.isidroid.b21.utils.FragmentConnector
import com.isidroid.b21.utils.FragmentResultListener
import timber.log.Timber

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindFragment() : Fragment(), LifecycleObserver, LifecycleOwner, BaseView, FragmentConnector, FragmentResultListener {
    override var currentFragment: Fragment? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag("fragment_lifecycle").i("onCreate ${javaClass.simpleName} act=${requireActivity().javaClass.simpleName}")
        lifecycle.addObserver(this)
        onCreateViewModel()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createBaseView()
        lifecycleScope.launchWhenResumed { onReady() }
    }

    override fun onResume() {
        super.onResume()
        Timber.tag("fragment_lifecycle").i("onResume ${javaClass.simpleName} act=${requireActivity().javaClass.simpleName}")
        fragmentConnector(true)
    }

    override fun onStart() {
        super.onStart()
        startFragmentResultListeners()
    }

    override fun onStop() {
        super.onStop()
        stopFragmentResultListeners()
    }

    @CallSuper
    override fun onHiddenChanged(hidden: Boolean) {
        Timber.tag("fragment_lifecycle").i("onHiddenChanged hidden=$hidden ${javaClass.simpleName} act=${requireActivity().javaClass.simpleName}")
        fragmentConnector(!hidden)
    }

    override fun onPause() {
        super.onPause()
        Timber.tag("fragment_lifecycle").i("onPause ${javaClass.simpleName} act=${requireActivity().javaClass.simpleName}")
        fragmentConnector(false)
        requireActivity().hideSoftKeyboard()
    }

    open fun onCreateViewModel() {}

    @CallSuper
    open fun showError(
        message: String?,
        isCritical: Boolean = false,
        buttonTitle: String? = null,
        onButtonClick: (() -> Unit)? = null
    ) {
        (requireActivity() as? CoreBindActivity)
            ?.showError(message, isCritical, buttonTitle, onButtonClick)
    }

    @CallSuper
    open fun showError(
        t: Throwable?,
        isCritical: Boolean = false,
        buttonTitle: String? = null,
        onButtonClick: (() -> Unit)? = null
    ) {
        (requireActivity() as? CoreBindActivity)
            ?.showError(t, isCritical, buttonTitle, onButtonClick)
    }

    private fun fragmentConnector(isResumed: Boolean) {
        val connector =
            parentFragment as? FragmentConnector
                ?: (parentFragment as? NavHostFragment)?.parentFragment as? FragmentConnector
                ?: activity as? FragmentConnector ?: return

        if (isResumed && !isHidden && this.isResumed) connector.onFragmentResumed(this)
        else connector.onFragmentPaused(this)
    }
}