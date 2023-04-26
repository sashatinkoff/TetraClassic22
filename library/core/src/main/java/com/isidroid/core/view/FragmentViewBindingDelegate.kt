package com.isidroid.core.view

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Create bindings for a view similar to bindView.
 *
 * To use, just call
 * private val binding: FHomeWorkoutDetailsBinding by viewBinding()
 * with your binding class and access it as you normally would.
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)


class FragmentViewBindingDelegate<T : ViewBinding>(
    bindingClass: Class<T>,
    val fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {
    private val clearBindingHandler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }
    private var binding: T? = null
    private val bindMethod = bindingClass.getDeclaredMethod("bind", View::class.java)

    init {
        val uiStateObserver = object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                // Lifecycle listeners are called before onDestroyView in Fragment
                // However, we want views to be able to use bindings in onDestroyView
                // to do cleanup so we clear the reference on frame later
                clearBindingHandler.post { binding = null }
            }
        }

        fragment.lifecycle.addObserver(uiStateObserver)
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding != null && binding?.root !== thisRef.view)
            binding = null

        binding?.also { return it }

        val lifecycle = fragment.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}")
        }

        binding = bindMethod.invoke(null, thisRef.requireView())?.cast<T>()
        return binding!!
    }
}

@Suppress("UNCHECKED_CAST")
internal fun <T> Any.cast(): T = this as T