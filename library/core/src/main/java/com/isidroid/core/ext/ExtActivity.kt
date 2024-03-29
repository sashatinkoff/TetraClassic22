package com.isidroid.core.ext

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

internal val Activity.rootView: View
    get() = findViewById(android.R.id.content)

fun Context?.alert(
    titleRes: Int? = null, messageRes: Int? = null,
    title: String? = null, message: String? = null,
    positiveRes: Int? = null, positive: String? = null,
    negativeRes: Int? = null, negative: String? = null,
    neutralRes: Int? = null, neutral: String? = null,
    onPositive: () -> Unit = {},
    onNeutral: () -> Unit = {},
    onNegative: () -> Unit = {},
    view: View? = null,
    items: Array<out CharSequence>? = null,
    onItemSelected: ((Int, String) -> Unit)? = null,
    isCancelable: Boolean = true,
    onDismiss: (() -> Unit)? = null
): AlertDialog? {
    this ?: return null
    val builder = AlertDialog.Builder(this)
        .setCancelable(isCancelable)

    view?.let { builder.setView(view) }
    onDismiss?.let { builder.setOnDismissListener { onDismiss() } }

    when {
        title != null -> builder.setTitle(title)
        titleRes != null -> builder.setTitle(titleRes)
    }

    when {
        view == null && message != null -> builder.setMessage(message)
        view == null && messageRes != null -> builder.setMessage(messageRes)
    }

    when {
        positive != null -> builder.setPositiveButton(positive) { _, _ -> onPositive() }
        positiveRes != null -> builder.setPositiveButton(positiveRes) { _, _ -> onPositive() }
    }

    when {
        negative != null -> builder.setNegativeButton(negative) { _, _ -> onNegative() }
        negativeRes != null -> builder.setNegativeButton(negativeRes) { _, _ -> onNegative() }
    }

    when {
        neutral != null -> builder.setNeutralButton(neutral) { _, _ -> onNeutral() }
        neutralRes != null -> builder.setNeutralButton(neutralRes) { _, _ -> onNeutral() }
    }

    items?.let {
        builder.setItems(items) { _, pos ->
            onItemSelected?.invoke(
                pos,
                items[pos].toString()
            )
        }
    }

    val result = builder.create()
    result.show()
    return result
}

fun FragmentActivity.findNavController(@IdRes viewId: Int): NavController {
    val navHostFragment = supportFragmentManager.findFragmentById(viewId) as NavHostFragment
    return navHostFragment.navController
}

fun Fragment.findNavController(@IdRes viewId: Int): NavController {
    val navHostFragment = childFragmentManager.findFragmentById(viewId) as NavHostFragment
    return navHostFragment.navController
}