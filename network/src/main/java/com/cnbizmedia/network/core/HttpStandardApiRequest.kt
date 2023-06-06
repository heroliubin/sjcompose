@file: JvmName("HttpStandardApiRequest")

package com.cnbizmedia.network.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cnbizmedia.network.core.exception.HttpApiErrorCoroutineHandler
import com.cnbizmedia.network.resp.ApiCallback
import kotlinx.coroutines.*

/**
 * 执行API请求
 * @param apiReq 应用请求
 * @param onResult 结果回调
 */
fun <T : Any> ViewModel.doApiHttpRegularRequest(
    apiReq: suspend () -> T?,
    onResult: (ApiCallback<T>) -> Unit = { _ -> }
): Job = viewModelScope.handleHttpApiRegularRequest(apiReq, onResult)

/**
 * 直接执行API请求，不建议使用
 * @param apiReq 应用请求
 * @param onResult 结果回调
 */
fun <T : Any> doApiHttpRegularRequest(
    apiReq: suspend () -> T?,
    onResult: (ApiCallback<T>) -> Unit = { _ -> }
): Job = CoroutineScope(Dispatchers.IO).handleHttpApiRegularRequest(apiReq, onResult)

/**
 * 启动协程进行标准的API调用
 * @param apiRequest 请求块
 * @param callback 请求结果回调
 */
private fun <T : Any> CoroutineScope.handleHttpApiRegularRequest(
    apiRequest: suspend () -> T?,
    callback: (ApiCallback<T>) -> Unit
) = launch(HttpApiErrorCoroutineHandler(callback)) {
    // if (ContextProvider.get().context?.isNetworkAvailable() != true)
    //     throw NetworkUnAvailableException()
    withContext(Dispatchers.Main) {
        callback(ApiCallback.Success(HttpResponseCode.HTTP_OK.toString(), null, apiRequest()))
    }
}