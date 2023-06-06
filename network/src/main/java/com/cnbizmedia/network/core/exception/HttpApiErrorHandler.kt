package com.cnbizmedia.network.core.exception

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.cnbizmedia.network.R
import com.cnbizmedia.network.core.HttpResponseCode
import com.cnbizmedia.network.provider.ContextProvider
import com.cnbizmedia.network.resp.ApiCallback
import kotlinx.coroutines.CoroutineExceptionHandler
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * 网络请求错误的协程处理类
 * @param errorCallback 错误回调
 */
class HttpApiErrorCoroutineHandler(
    private val errorCallback: (ApiCallback.Error) -> Unit
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        // context.cancel() //取消内容的请求
        val err = exception.handleHttpErrorMessage()
        errorCallback(ApiCallback.Error(err.code, err.message))
    }

}

/** 处理网络相关异常信息 **/
fun Throwable.handleHttpErrorMessage(): HttpApiException {
    val contextInstance = ContextProvider.get().context
    return (when (this) {
        is UnknownHostException -> HttpApiException(
            HttpResponseCode.HTTP_UNKNOWN_HOST.toString(),
            contextInstance?.getString(R.string.net_state_no_host)
        )
        is ConnectException -> HttpApiException(
            HttpResponseCode.HTTP_BAD_REQUEST.toString(),
            contextInstance?.getString(R.string.net_state_disconnect)
        )
        is SocketTimeoutException -> HttpApiException(
            HttpResponseCode.HTTP_CLIENT_TIMEOUT.toString(),
            contextInstance?.getString(R.string.net_state_connect_timeout)
        )
        is HttpException -> when (code()) {
            HttpResponseCode.HTTP_UNAUTHORIZED -> HttpApiException(
                HttpResponseCode.HTTP_UNAUTHORIZED.toString(),
                contextInstance?.getString(R.string.net_state_unauthorized)
            )
            HttpResponseCode.HTTP_NOT_FOUND ->
                HttpApiException(
                    HttpResponseCode.HTTP_NOT_FOUND.toString(),
                    contextInstance?.getString(R.string.net_state_not_found)
                )
            HttpResponseCode.HTTP_BAD_REQUEST ->
                HttpApiException(
                    HttpResponseCode.HTTP_BAD_REQUEST.toString(),
                    contextInstance?.getString(R.string.net_state_operate_too_many_times)
                )
            HttpResponseCode.HTTP_INTERNAL_ERROR ->
                HttpApiException(
                    HttpResponseCode.HTTP_INTERNAL_ERROR.toString(),
                    contextInstance?.getString(R.string.net_state_server_err)
                )
            HttpResponseCode.HTTP_BAD_GATEWAY ->
                HttpApiException(
                    HttpResponseCode.HTTP_BAD_GATEWAY.toString(),
                    contextInstance?.getString(R.string.net_state_bad_gateway)
                )
            HttpResponseCode.HTTP_UNAVAILABLE ->
                HttpApiException(
                    HttpResponseCode.HTTP_UNAVAILABLE.toString(),
                    contextInstance?.getString(R.string.net_state_http_unavailable)
                )
            HttpResponseCode.HTTP_GATEWAY_TIMEOUT ->
                HttpApiException(
                    HttpResponseCode.HTTP_CLIENT_TIMEOUT.toString(),
                    contextInstance?.getString(R.string.net_state_connect_timeout)
                )
            else -> HttpApiException(
                HttpResponseCode.HTTP_UNKNOWN_EXCEPTION.toString(),
                message ?: message()
            )
        }
        is MalformedJsonException,
        is JsonIOException ->
            HttpApiException(
                HttpResponseCode.HTTP_CANNOT_HANDLE_RESULT.toString(),
                contextInstance?.getString(R.string.net_state_server_analysis_err)
            )
        is JsonSyntaxException ->
            HttpApiException(
                HttpResponseCode.HTTP_JSON_SYNTAX_ERROR.toString(),
                contextInstance?.getString(R.string.net_state_json_syntax_err)
            )
        is HttpApiException -> this
        else -> {
            var outputMessage =
                contextInstance?.getString(R.string.net_state_request_failed, message)
            if (outputMessage?.lowercase()?.contains("was cancelled") == true)
                outputMessage = "" //忽略任务被取消后抛出异常的这种提醒消息
            HttpApiException(HttpResponseCode.HTTP_UNKNOWN_EXCEPTION.toString(), outputMessage)
        }
    })

}