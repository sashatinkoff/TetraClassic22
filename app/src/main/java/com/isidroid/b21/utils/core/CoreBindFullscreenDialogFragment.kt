package com.isidroid.b21.utils.core

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialogFragment
import com.isidroid.b21.R

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindFullscreenDialogFragment() : AppCompatDialogFragment(), IBaseView {
    open val canceledOnTouchOutside: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateViewModel()
    }

    protected open fun onCreateViewModel() {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                this@CoreBindFullscreenDialogFragment.onBackPressed()
            }

        }.apply {
            setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle)
            setCanceledOnTouchOutside(canceledOnTouchOutside)
        }
    }

    protected open fun onBackPressed() {
        Handler(Looper.myLooper()!!).postDelayed({ dismissAllowingStateLoss() }, 100)
    }

    override fun onResume() {
        dialog?.window?.apply {
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        super.onResume()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val children = (view as? ViewGroup)?.childCount ?: 0

        if (children > 0) {
            val parentView = view as? ViewGroup
            parentView?.removeAllViews()
        }

        if (canceledOnTouchOutside)
            view.setOnClickListener { dismissAllowingStateLoss() }

        createBaseView()
    }

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