package com.junction.savebears.component

data class UiState<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): UiState<T> {
            return UiState(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): UiState<T> {
            return UiState(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): UiState<T> {
            return UiState(Status.LOADING, data, null)
        }

        fun <T> empty(data: T? = null): UiState<T> {
            return UiState(Status.EMPTY, data, null)
        }
    }

}