package com.dysen.kotlin_demo.async

import android.os.Handler
import kotlin.coroutines.experimental.Continuation

/**
 * Created by benny on 5/29/17.
 */
class UiCotinuationWrapper<T>(val continuation: Continuation<T>): Continuation<T>{
    override val context = continuation.context
    var handler = Handler();

    override fun resume(value: T) {
        handler.postDelayed(Runnable {
            continuation.resume(value)
        }, 0)
    }

    override fun resumeWithException(exception: Throwable) {
        handler.post(Runnable {
            continuation.resumeWithException(exception) })
    }

}
