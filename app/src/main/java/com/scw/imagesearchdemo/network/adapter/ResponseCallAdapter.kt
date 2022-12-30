package com.scw.twtour.network.adapter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/***
 * Ref：https://github.com/MohammadSianaki/Retrofit2-Flow-Call-Adapter
 */
class ResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {

    override fun adapt(call: Call<T>): Flow<Response<T>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            continuation.resume(response)
                        }
                    })

                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}


class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.clone().also { c ->
                        c.enqueue(object : Callback<T> {
                            override fun onFailure(call: Call<T>, t: Throwable) {
                                continuation.resumeWithException(t)
                            }

                            override fun onResponse(call: Call<T>, response: Response<T>) {
                                try {
                                    response.body()?.also { continuation.resume(it) }
                                } catch (e: Exception) {
                                    continuation.resumeWithException(e)
                                }
                            }
                        })
                        continuation.invokeOnCancellation { c.cancel() }
                    }
                }
            )
        }
    }

    override fun responseType() = responseType
}