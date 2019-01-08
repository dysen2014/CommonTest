package com.dysen.kotlin_demo.async

import com.dysen.common_library.utils.JsonUtils
import com.dysen.kotlin_demo.common.HttpError
import com.dysen.kotlin_demo.common.HttpException
import com.dysen.kotlin_demo.common.log
import com.dysen.kotlin_demo.https.HttpService
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.startCoroutine
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * 开始协程
 */
fun startCoroutine(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) {
    block.startCoroutine(ContextContinuation(context + AsyncContext()))
}

/**
 * 开始耗时操作
 */
suspend fun <T> startExpensiveOperations(block: CoroutineContext.() -> T) = suspendCoroutine<T> {
    continuation ->
    log("异步任务开始前")
    AsyncTask {
        try {
            continuation.resume(block(continuation.context))
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }.execute()
}

/**
 * 开始加载图片
 */
fun startLoadImg(url: String): ByteArray {
    log("异步任务开始")
    log("耗时操作，下载图片")
    val responseBody = HttpService.service.getLogo(url).execute()
    if (responseBody.isSuccessful) {
        responseBody.body()?.byteStream()?.readBytes()?.let {
            return it
        }
        throw HttpException(HttpError.HTTP_ERROR_NO_DATA)
    } else {
        throw HttpException(responseBody.code())
    }
}

/**
 * 请求数据接口
 */
inline fun <reified T> getDatas(url: String): T? {
    log("异步任务开始")
    log("耗时操作，下载图片")
    val responseBody = HttpService.service.getLogo(url).execute()
    if (responseBody.isSuccessful) {
        var data = responseBody.body()?.string()
        log("Post -- > \nrespons datas===" + data)
        return JsonUtils.parseObject(data, T::class.java)
        throw HttpException(HttpError.HTTP_ERROR_NO_DATA)
    } else {
        throw HttpException(responseBody.code())
    }
}