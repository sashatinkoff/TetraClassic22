package com.isidroid.core.ui.core

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.isidroid.core.ext.alert
import com.isidroid.core.utils.FragmentConnector
import timber.log.Timber

/**
 * this class contains the base implementation, do not modify it.
 * To extend the class with your logic use base/Bind*.kt class
 *
 */
abstract class CoreBindActivity : AppCompatActivity(), BaseView, FragmentConnector {
    override var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.tag("activity_lifecycle").i("${javaClass.simpleName} onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
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
    open fun showError(t: Throwable) {
        Toast.makeText(this, t.message, Toast.LENGTH_LONG).show()
    }
}