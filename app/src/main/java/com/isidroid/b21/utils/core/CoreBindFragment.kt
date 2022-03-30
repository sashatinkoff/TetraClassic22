package com.isidroid.b21.utils.core

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.isidroid.b21.ext.hideSoftKeyboard
import timber.log.Timber

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindFragment() : Fragment(), LifecycleObserver, LifecycleOwner, IBaseView {

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
    }

    override fun onPause() {
        super.onPause()
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
}