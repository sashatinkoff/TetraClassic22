package com.isidroid.core.ui.core

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.CallSuper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.isidroid.core.R
import com.isidroid.core.ui.ErrorUiHandler

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindBottomSheetDialogFragment : BottomSheetDialogFragment, BaseView, ErrorUiHandler {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    protected open val noDim: Boolean = false
    protected open val fullScreenHeight: Boolean = false

    open val getFullScreenHeight: Int
        get() = WindowManager.LayoutParams.MATCH_PARENT

    open val dismissOnTouchOutside: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        onCreateViewModel()
    }


    override fun onStart() {
        super.onStart()
        if (noDim)
            dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            val color = ColorDrawable(Color.BLACK)
            color.alpha = 10
            window?.setBackgroundDrawable(color)

            setOnShowListener { onCreateBottomSheetDialog(dialog as BottomSheetDialog) }
        }
    }

    private fun onCreateBottomSheetDialog(dialog: BottomSheetDialog) {
        dialog.dismissWithAnimation = true
        dialog.setCanceledOnTouchOutside(dismissOnTouchOutside)

        if (fullScreenHeight) {
            val parentLayout =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                val layoutParams = parentLayout.layoutParams
                layoutParams.height = getFullScreenHeight
                parentLayout.layoutParams = layoutParams
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createBaseView()
    }

    protected open fun onCreateViewModel() {}

    override fun showError(t: Throwable?) {
        (activity as? ErrorUiHandler)?.showError(t)
    }
}

