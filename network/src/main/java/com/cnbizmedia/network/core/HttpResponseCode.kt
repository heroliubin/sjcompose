@file: JvmName("HttpResponseCode")

package com.cnbizmedia.network.core

import java.net.HttpURLConnection

/** 网络连接反馈的状态码 **/
object HttpResponseCode {

    /** 未知异常信息，错误码：9000 **/
    const val HTTP_UNKNOWN_EXCEPTION = 9000

    /** 无法处理结果，数据解析失败：9001 **/
    const val HTTP_CANNOT_HANDLE_RESULT = 9001

    /** 网络无法可用，错误码：9002  **/
    const val HTTP_NETWORK_UNAVAILABLE = 9002

    /** 未知的HOST地址，错误码：9003  **/
    const val HTTP_UNKNOWN_HOST = 9003

    /** JSON数据解析格式错误，错误码：9004  **/
    const val HTTP_JSON_SYNTAX_ERROR = 9004

    /** 网络请求成功：200 **/
    const val HTTP_OK = HttpURLConnection.HTTP_OK

    /** HTTP Status-Code 400: Bad Request. [HttpURLConnection.HTTP_BAD_REQUEST] **/
    const val HTTP_BAD_REQUEST = HttpURLConnection.HTTP_BAD_REQUEST

    /** HTTP Status-Code 401: Unauthorized. [HttpURLConnection.HTTP_UNAUTHORIZED] **/
    const val HTTP_UNAUTHORIZED = HttpURLConnection.HTTP_UNAUTHORIZED

    /** HTTP Status-Code 404: Not Found. [HttpURLConnection.HTTP_NOT_FOUND] **/
    const val HTTP_NOT_FOUND = HttpURLConnection.HTTP_NOT_FOUND

    /** HTTP Status-Code 408: Request Time-Out. [HttpURLConnection.HTTP_CLIENT_TIMEOUT] **/
    const val HTTP_CLIENT_TIMEOUT = HttpURLConnection.HTTP_CLIENT_TIMEOUT

    /** Internal Server Error：500 [HttpURLConnection.HTTP_INTERNAL_ERROR] **/
    const val HTTP_INTERNAL_ERROR = HttpURLConnection.HTTP_INTERNAL_ERROR

    /** HTTP Status-Code 502: Bad Gateway. **/
    const val HTTP_BAD_GATEWAY = HttpURLConnection.HTTP_BAD_GATEWAY

    /** HTTP Status-Code 503: Service Unavailable. **/
    const val HTTP_UNAVAILABLE = HttpURLConnection.HTTP_UNAVAILABLE

    /** HTTP Status-Code 503: Service Unavailable. **/
    const val HTTP_GATEWAY_TIMEOUT = HttpURLConnection.HTTP_GATEWAY_TIMEOUT


}