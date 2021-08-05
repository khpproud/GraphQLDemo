package dev.patrick.graphqldemo.view.state

sealed class ViewState<out T>(
    val value: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ViewState<T>(data)
    class Error<T>(message: String?, data: T? = null) : ViewState<T>(data, message)
    object Loading : ViewState<Nothing>()
}