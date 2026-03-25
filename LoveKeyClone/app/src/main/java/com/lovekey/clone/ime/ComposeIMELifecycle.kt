package com.lovekey.clone.ime

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

/**
 * A dummy LifecycleOwner and SavedStateRegistryOwner needed for Compose in an InputMethodService.
 * Without these, setContent {} will crash because it expects an Activity context.
 */
class ComposeIMELifecycle : LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var savedStateRegistryController: SavedStateRegistryController =
        SavedStateRegistryController.create(this)
    private val store = ViewModelStore()
    private var isCreated = false

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = store

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    fun onCreate() {
        if (!isCreated) {
            savedStateRegistryController.performRestore(Bundle())
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            isCreated = true
        }
    }

    fun onStart() {
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }
    }

    fun onResume() {
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }
    }

    fun onPause() {
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        }
    }

    fun onStop() {
        if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    fun onDestroy() {
        if (isCreated) {
            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            store.clear()
            isCreated = false
        }
    }
}
