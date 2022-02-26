package com.kurilov.worldscillscomponent.data.classes

data class MResult<out T>(val status : MStatus, val data : T?, val message : String?) {
    companion object {
        fun <T> success(data : T?) : MResult<T> = MResult(status = MStatus.SUCCESS, data = data, message = null)

        fun <T> error(data : T?, message : String) : MResult<T> =
            MResult(status = MStatus.ERROR, data = data, message = message)

        fun <T> loading(data : T?) : MResult<T> = MResult(status = MStatus.LOADING, data = data, message = null)
    }
}