package com.isidroid.b21.utils

import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

interface FragmentConnector {
    var currentFragment: Fragment?

    @CallSuper
    fun onFragmentResumed(fr: Fragment) {
        this.currentFragment = fr
    }

    @CallSuper
    fun onFragmentPaused(fr: Fragment) {
    }
}