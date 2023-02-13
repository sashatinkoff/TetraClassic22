package com.isidroid.b21.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import com.isidroid.b21.ui.main.MainView
import com.isidroid.core.ext.parent
import com.isidroid.core.ui.NavigationListener
import com.isidroid.core.ui.StatusColorListener
import com.isidroid.core.ui.core.CoreBindFragment

abstract class BindFragment : CoreBindFragment(), NavigationListener, StatusColorListener {
    override val statusBarColorRes: Int = android.R.color.transparent
    override val navigationBarColorRes: Int = android.R.color.transparent
    override val isLightStatusBarIcons: Boolean = (activity as? MainView)?.isNightMode != true

    override val navController: NavController
        get() {
            val navigationListener = (parent as? NavigationListener) ?: (activity as NavigationListener)
            return navigationListener.navController
        }

    protected val topInset get() = (activity as? MainView)?.topInset ?: 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = super.onCreateView(inflater, container, savedInstanceState)
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainView)?.updateProgress(false)

    }

    @CallSuper
    open fun updateProgress(isProgress: Boolean) {
        (activity as? MainView)?.updateProgress(isProgress)
    }

    override fun showError(t: Throwable) {
        updateProgress(false)
        super.showError(t)
    }
}