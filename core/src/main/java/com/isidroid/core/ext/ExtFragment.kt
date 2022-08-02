package com.isidroid.core.ext

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.isidroid.core.utils.ParcelArg
import java.io.Serializable

fun Fragment.put(key: String, value: Any?) =
    apply { putArgument(fragment = this, key = key, value = value) }

fun DialogFragment.put(key: String, value: Any?) =
    apply { putArgument(fragment = this, key = key, value = value) }

fun Fragment.putObjects(key: String, value: List<Parcelable>?) =
    apply {
        value ?: return@apply

        arguments = arguments ?: Bundle()
        arguments?.putParcelable(key, ParcelArg(value))
    }

fun DialogFragment.putObjects(key: String, value: List<Parcelable>?) =
    apply {
        value ?: return@apply

        arguments = arguments ?: Bundle()
        arguments?.putParcelable(key, ParcelArg(value))
    }

fun DialogFragment.showDialog(
    activity: FragmentActivity? = null,
    fragment: Fragment? = null,
    tag: String = this.javaClass.simpleName
) {
    when {
        activity != null -> (activity.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dialog?.isShowing == true
        fragment != null -> (fragment.childFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dialog?.isShowing == true
        else -> null
    }?.let { isShowing ->
        if (!isShowing) {
            (activity?.supportFragmentManager ?: fragment?.childFragmentManager)
                ?.apply { show(this, tag) }

        }
    }
}

fun Bundle.putArgument(key: String, value: Any?) = apply {
    when (value) {
        is String -> putString(key, value)
        is Serializable -> putSerializable(key, value)
        is Long -> putLong(key, value)
        is Double -> putDouble(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Parcelable -> putParcelable(key, value)
    }
}

private fun putArgument(fragment: Fragment, key: String, value: Any?) = fragment.apply {
    value ?: return@apply
    arguments = arguments ?: Bundle()
    arguments?.putArgument(key, value)
}

fun Fragment.findNavController(@IdRes viewId: Int): NavController {
    val navHostFragment = childFragmentManager.findFragmentById(viewId) as NavHostFragment
    return navHostFragment.navController
}

val Fragment.parent get() = (parentFragment as? NavHostFragment)?.parentFragment ?: parentFragment