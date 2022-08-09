package com.isidroid.core.ui

import android.app.Activity
import android.app.ActivityOptions
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable

open class Router(
    private val caller: Any?,
    private val activity: Class<out Activity>? = null,
    private val closeCurrent: Boolean = false,
    private val closeAll: Boolean = false,
    private val isAnimate: Boolean = false,
    private val requestCode: Int? = null,
    private val sceneTransitionAnimation: ((Activity?) -> ActivityOptions)? = null,
    private val onIntent: ((Intent) -> Unit)? = null,
    private val intent: Intent? = null,
    private val action: String? = null,
    private val noAnimation: Boolean = false
) {
    private val context: Context?
        get() = when (caller) {
            is Activity -> caller
            is Service -> caller
            is Fragment -> caller.requireActivity()
            else -> throw IllegalStateException("Caller is not a context")
        }

    private val arguments = hashMapOf<String, Any?>()
    fun putExtra(key: String, value: Any?) = apply { arguments[key] = value }

    private fun openActivity() {
        val intent = intent ?: Intent(context, activity)
        onIntent?.invoke(intent)
        action?.let { intent.setAction(action) }

        arguments.keys.forEach { key ->
            val value = arguments[key]

            (value as? String)?.let { intent.putExtra(key, value) }
            (value as? Int)?.let { intent.putExtra(key, value) }
            (value as? Double)?.let { intent.putExtra(key, value) }
            (value as? Long)?.let { intent.putExtra(key, value) }
            (value as? Serializable)?.let { intent.putExtra(key, value) }
            (value as? Parcelable)?.let { intent.putExtra(key, value) }
            (value as? Array<*>)?.also { intent.putExtra(key, value) }
        }

        val options: Bundle? = (context as? Activity)?.let { activity ->
            if (isAnimate)
                (sceneTransitionAnimation?.invoke(activity)
                    ?: ActivityOptions.makeSceneTransitionAnimation(activity)).toBundle()
            else null
        }

        if (closeAll) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        when (caller) {
            is Activity -> activityStarts(
                activity = caller,
                intent = intent,
                options = options
            )
            is Fragment -> fragmentStarts(fragment = caller, intent = intent, options = options)
            is Service -> caller.startActivity(intent)
        }

        (context as? Activity)?.apply { if (closeCurrent) finish() }
    }

    private fun activityStarts(activity: Activity, intent: Intent, options: Bundle?) =
        activity.apply {
            requestCode
                ?.let { startActivityForResult(intent, requestCode, options) }
                ?: run { startActivity(intent, options) }

            if(noAnimation) overridePendingTransition(0, 0)
        }

    private fun fragmentStarts(fragment: Fragment, intent: Intent, options: Bundle?) =
        fragment.apply {
            requestCode
                ?.let { startActivityForResult(intent, requestCode, options) }
                ?: run { startActivity(intent, options) }

            if(noAnimation) requireActivity().overridePendingTransition(0, 0)
        }

    fun open() {
        context ?: throw IllegalStateException("Caller is null")
        if (activity != null || intent != null) openActivity()
    }
}