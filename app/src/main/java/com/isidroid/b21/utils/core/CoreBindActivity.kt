package com.isidroid.b21.utils.core

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.isidroid.b21.data.source.settings.Settings
import com.isidroid.b21.ext.alert
import com.isidroid.b21.utils.FragmentConnector
import timber.log.Timber

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindActivity : AppCompatActivity(), BaseView, FragmentConnector {
    private var errorDialog: AlertDialog? = null

    override var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(Settings.theme)
        Timber.tag("activity_lifecycle").i("${javaClass.simpleName} onCreate")
        super.onCreate(savedInstanceState)
        createBaseView()
        onCreateViewModel()
    }

    override fun onResume() {
        super.onResume()
        Timber.tag("activity_lifecycle").i("${javaClass.simpleName} onResume")
    }

    override fun onDestroy() {
        super.onDestroy()

        Timber.tag("activity_lifecycle").i("${javaClass.simpleName} onDestroy")
    }

    open fun onCreateViewModel() {}

    @CallSuper
    open fun showError(
        t: Throwable?,
        isCritical: Boolean = false,
        buttonTitle: String? = null,
        onButtonClick: (() -> Unit)? = null
    ) {
        showError(
            message = t?.message,
            isCritical = isCritical,
            buttonTitle = buttonTitle,
            onButtonClick = onButtonClick
        )
    }

    @CallSuper
    open fun showError(
        message: String?,
        isCritical: Boolean = false,
        buttonTitle: String? = null,
        onButtonClick: (() -> Unit)? = null
    ) {
        message ?: return

        if (isCritical && errorDialog?.isShowing != true)
            errorDialog = alert(
                message = message,
                positive = buttonTitle,
                onPositive = { onButtonClick?.invoke() },
                onDismiss = { errorDialog = null },
                negativeRes = android.R.string.cancel,
                isCancelable = false
            )
        else if (!isCritical)
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}