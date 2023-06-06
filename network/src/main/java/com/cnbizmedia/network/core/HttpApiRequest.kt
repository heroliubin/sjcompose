@file: JvmName("HttpApiRequest")

package com.cnbizmedia.network.core


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnbizmedia.network.core.exception.HttpApiErrorCoroutineHandler
import com.cnbizmedia.network.core.exception.HttpApiException
import com.cnbizmedia.network.resp.ApiCallback
import com.cnbizmedia.network.resp.IBasicRespInfo
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 执行API请求
 * @param apiReq 应用请求
 * @param onResult 结果回调
 */
fun <T : Any> ViewModel.doHttpApiRequest(
    apiReq: suspend () -> IBasicRespInfo<T>,
    onResult: (ApiCallback<T>) -> Unit = { _ -> }
): Job = viewModelScope.handleHttpApiRequest(apiReq, onResult)

/**
 * 简写【viewModelScope.launch】方法
 * @param context 上下文对象
 * @param start 启动方式
 * @param block 阻塞方法
 */
fun ViewModel.launchInScope(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(context, start, block)

/**
 * 直接执行API请求，不建议使用
 * @param apiReq 应用请求
 * @param onResult 结果回调
 */
fun <T : Any> doHttpApiRequest(
    apiReq: suspend () -> IBasicRespInfo<T>,
    onResult: (ApiCallback<T>) -> Unit = { _ -> }
): Job = CoroutineScope(Dispatchers.IO).handleHttpApiRequest(apiReq, onResult)

/**
 * 启动协程进行API调用
 * @param apiRequest 请求块
 * @param callback 结果请求回调
 */
private fun <T : Any> CoroutineScope.handleHttpApiRequest(
    apiRequest: suspend () -> IBasicRespInfo<T>,
    callback: (ApiCallback<T>) -> Unit
) = launch(HttpApiErrorCoroutineHandler(callback)) {
    // if (ContextProvider.get().context?.isNetworkAvailable() != true)
    //     throw NetworkUnAvailableException()
    val result = apiRequest()
    withContext(Dispatchers.Main) {
        if (result.isSuccessful)
            callback(ApiCallback.Success(result.code, result.message, result.data))
        else throw HttpApiException(result.code, result.message, result.data)
    }
}

