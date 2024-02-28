package dev.jorgealejandro.tm.discoveryapi.challenge.base

import androidx.lifecycle.ViewModel
import dev.jorgealejandro.tm.discoveryapi.challenge.util.UIEvent
import dev.jorgealejandro.tm.discoveryapi.challenge.util.launch
import kotlinx.coroutines.Dispatchers

abstract class BaseViewModel : ViewModel() {
    abstract fun onUIEvent(event: UIEvent)
    protected fun exec(action: suspend () -> Unit) {
        launch(Dispatchers.IO) { action() }
    }
}