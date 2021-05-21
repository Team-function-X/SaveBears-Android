package com.junction.savebears.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Retrofit 의 Call 값을 CallbackFlow 로 컨버팅해줌
 * - close() 에 Throwable 형태 인자를 넘겨주면 Flow 스트림에서 catch 로 잡을 수 있음
 */
@ExperimentalCoroutinesApi
fun <T> Call<T>.asCallbackFlow() = callbackFlow<T> {
    enqueue(object : Callback<T> {
        // 응답을 성공적으로 받은 경우
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let { offer(it) } ?: close(EmptyBodyError("데이터가 비어있습니다"))
            } else {
                close(FailNetworkError("${response.code()} + ${response.errorBody()}"))
            }
        }

        // API 호출을 예기치 않게 실패한 경우
        override fun onFailure(call: Call<T>, throwable: Throwable) {
            close(throwable)
        }
    })

    awaitClose() //close 가 호출될때까지 기다립니다.
}