@file: JvmName("HttpApiManagerConfig")

package com.cnbizmedia.network.core.manager

import okhttp3.Interceptor


/** API网络配置实体类 **/
data class HttpApiManagerConfig(

    /** API域名地址 **/
    @JvmField
    val baseUrl: String = "",

    @JvmField
    /** Http的Interceptors，只会在请求中被处理一次 **/
    val httpInterceptors: List<Interceptor> = mutableListOf(),

    /** Http的Interceptors **/
    val interceptors: List<Interceptor> = mutableListOf(),

    @JvmField
    /** 链接超时时间,默认15s **/
    var connectTimeout: Long = 15,

    @JvmField
    /** 读取超时时间,默认15s **/
    var readTimeout: Long = 60,

    @JvmField
    /** 写入数据超时,默认60s **/
    val writeTimeout: Long = 60,

    /** 设置为无代理模式 **/
    @JvmField
    val noProxy: Boolean = false
)