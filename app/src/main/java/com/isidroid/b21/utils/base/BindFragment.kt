package com.isidroid.b21.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.navigation.NavController
import com.isidroid.b21.ui.main.MainView
import com.isidroid.core.ext.parent
import com.isidroid.core.ui.ErrorUiHandler
import com.isidroid.core.ui.NavigationListener
import com.isidroid.core.ui.ScreenUiListener
import com.isidroid.core.ui.StatusColorListener
import com.isidroid.core.ui.core.CoreBindFragment
import com.isidroid.core.utils.KeyboardVisibilityListener
import com.isidroid.core.utils.WindowInsetListener

abstract class BindFragment : CoreBindFragment, NavigationListener, StatusColorListener, WindowInsetListener, KeyboardVisibilityListener, ErrorUiHandler {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override val statusBarColorRes: Int get() = com.isidroid.core.R.attr.color_transparent
    override val navigationBarColorRes: Int get() = com.google.android.material.R.attr.colorSurface
    override val isLightStatusBarIcons: Boolean get() = (activity as? ScreenUiListener)?.isNightMode != true

    override val navController: NavController
        get() {
            val navigationListener = (parent as? NavigationListener) ?: (activity as NavigationListener)
            return navigationListener.navController
        }

    protected val topInset get() = (activity as? ScreenUiListener)?.topInset ?: 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = super.onCreateView(inflater, container, savedInstanceState)
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainView)?.updateProgress(false)

    }

    @CallSuper
    open fun updateProgress(isProgress: Boolean) {
        (activity as? MainView)?.updateProgress(isProgress)
    }

    override fun showError(t: Throwable?) {
        updateProgress(false)
        (activity as? ErrorUiHandler)?.showError(t)
    }
}