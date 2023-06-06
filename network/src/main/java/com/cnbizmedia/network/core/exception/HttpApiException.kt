package com.cnbizmedia.network.core.exception

import com.google.gson.Gson
import com.cnbizmedia.network.R
import com.cnbizmedia.network.core.HttpResponseCode
import com.cnbizmedia.network.provider.ContextProvider


/**
 * 网络API请求异常
 * @param code 错误码，取值参考：[HttpResponseCode]
 * @param message 错误描述
 * @param data 数据内容
 */
open class HttpApiException(
    val code: String?,
    message: String?,
    val data: Any? = null
) : Throwable(message) {

    override fun toString() =
        "{\"code\": $code, \"message\":\"$message\", \"data\": ${Gson().toJson(data)}}"

}

/** 网络不可用的异常 **/
class NetworkUnAvailableException : HttpApiException(
    code = HttpResponseCode.HTTP_NETWORK_UNAVAILABLE.toString(),
    message = ContextProvider.get().context?.getString(R.string.net_state_unavailable)
)