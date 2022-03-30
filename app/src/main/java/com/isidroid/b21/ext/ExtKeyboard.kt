package com.isidroid.b21.ext

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlin.math.roundToInt


fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    val errorValue =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)

    rootView.getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = rootView.height - visibleBounds.height()
    val marginOfError = errorValue.roundToInt()
    return heightDiff > marginOfError
}

fun FragmentActivity.hideSoftKeyboard() {
    try {
        var windowToken: IBinder? = null
        val fm: FragmentManager = supportFragmentManager
        if (fm.fragments.size > 0) {
            windowToken = fm.fragments[fm.fragments.size - 1]?.view?.windowToken
        }

        if (windowToken == null && currentFocus != null)
            windowToken = currentFocus?.windowToken


        windowToken?.also { wt ->
            val currentFocus = currentFocus
            val mImm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mImm.hideSoftInputFromWindow(wt, 0)
            if (currentFocus != null) mImm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

    } catch (e: Exception) {
    }
}


fun Context.showSoftKeyboard() {
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.hideSoftKeyboard() {
    (context as? FragmentActivity)?.hideSoftKeyboard()
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}