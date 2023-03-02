package com.isidroid.b21.utils.base

import androidx.navigation.NavController
import com.isidroid.core.ext.parent
import com.isidroid.core.ui.NavigationListener
import com.isidroid.core.ui.core.CoreBindBottomSheetDialogFragment

abstract class BindBottomSheetDialogFragment : CoreBindBottomSheetDialogFragment(), NavigationListener {
    override val navController: NavController
        get() {
            val navigationListener = (parent as? NavigationListener) ?: (activity as NavigationListener)
            return navigationListener.navController
        }
}