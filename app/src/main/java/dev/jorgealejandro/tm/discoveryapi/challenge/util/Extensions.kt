package dev.jorgealejandro.tm.discoveryapi.challenge.util

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dev.jorgealejandro.tm.discoveryapi.challenge.ui.states.DataPresentationState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * [ViewModel.launch]
 * Extension function that simplifies the launch of coroutines in a ViewModel.
 *
 * This extension function takes a coroutine dispatcher as an optional parameter, defaulting
 * to [Dispatchers.IO]. It then launches a coroutine in the [viewModelScope] using the
 * provided dispatcher.
 *
 * This extension function is useful for optimizing the launch structure per each instance
 * in the ViewModel. It allows you to easily launch coroutines in the appropriate dispatcher
 * without having to manually create a [CoroutineScope] or specify the dispatcher each time.
 *
 * @param dispatcher The coroutine dispatcher to use for the launched coroutine.
 * @param block The coroutine block to be executed.
 * @return A [Job] representing the launched coroutine.
 */
inline fun ViewModel.launch(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend CoroutineScope.() -> Unit
): Job {
    val executor: CoroutineContext by lazy { dispatcher }
    return viewModelScope.launch(executor) { block() }
}

/**
 * [Flow.asRemotePresentationState]
 * Extension to Converts a flow of [CombinedLoadStates] into a flow of [DataPresentationState].
 *
 * This function is used to map the combined load states of a [Pager] that uses a [RemoteMediator]
 * and a [PagingSource] to a simplified presentation state.
 *
 * The presentation state is used to indicate the current state of the data being
 * presented to the user.
 *
 * The mapping is done under the assumption that successful [RemoteMediator] fetches always
 * cause invalidation of the [PagingSource], as is the case with the [PagingSource]
 * provided by Room.
 *
 * The presentation state can be one of the following:
 *
 * - DataPresentationState.INITIAL: The initial state, before any data is loaded.
 * - DataPresentationState.REMOTE_LOADING: Data is being loaded from the remote source.
 * - DataPresentationState.SOURCE_LOADING: Data is being loaded from the local source.
 * - DataPresentationState.PRESENTED: Data is ready to be presented.
 *
 * @return A flow of [DataPresentationState] objects.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun Flow<CombinedLoadStates>.asRemotePresentationState(): Flow<DataPresentationState> =
    scan(DataPresentationState.INITIAL) { state, loadState ->
        when (state) {
            DataPresentationState.PRESENTED -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> DataPresentationState.REMOTE_LOADING
                else -> state
            }

            DataPresentationState.INITIAL -> when (loadState.mediator?.refresh) {
                is LoadState.Loading -> DataPresentationState.REMOTE_LOADING
                else -> state
            }

            DataPresentationState.REMOTE_LOADING -> when (loadState.source.refresh) {
                is LoadState.Loading -> DataPresentationState.SOURCE_LOADING
                else -> state
            }

            DataPresentationState.SOURCE_LOADING -> when (loadState.source.refresh) {
                is LoadState.NotLoading -> DataPresentationState.PRESENTED
                else -> state
            }
        }
    }.distinctUntilChanged()


/**
 * [ImageView.setImage]
 * Sets the image of the ImageView using Glide with the given String URL.
 *
 * @param path The Bitmap to be set as the image.
 */
fun ImageView.setImage(path: String?) {
    path?.let {
        Glide.with(this.context)
            .asBitmap()
            .load(path)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(this)
    }
}

/**
 * [View.showKeyboard]
 * Show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}


/**
 * [View.hideKeyboard]
 * Hide the keyboard and returns whether it worked.
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}