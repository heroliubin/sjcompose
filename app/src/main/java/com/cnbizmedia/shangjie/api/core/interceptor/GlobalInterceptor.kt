package com.cnbizmedia.shangjie.api.core.interceptor

import com.cnbizmedia.comman.app.LogUtils
import com.cnbizmedia.network.core.HttpResponseCode
import com.cnbizmedia.network.core.manager.HttpApiManager
import com.cnbizmedia.network.utils.isNetworkAvailable
import com.cnbizmedia.shangjie.app.SjApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.cacheGet

/** API的Interceptor **/
class GlobalInterceptor : Interceptor {

    companion object {

        /** 获取网络日志打印的Interceptor, 建议Debug模式下调用 **/
        val httpLoggingInterceptor: Interceptor
            @JvmStatic get() {
                val levelClazz = Class.forName("okhttp3.logging.HttpLoggingInterceptor\$Level")
                val levelValue = levelClazz.getField("BODY")
                    .apply { isAccessible = true }
                    .get(null)
                val clazz = Class.forName("okhttp3.logging.HttpLoggingInterceptor")
                val interceptor = clazz.newInstance()
                clazz.getMethod("setLevel", levelClazz)
                    .apply { isAccessible = true }
                    .invoke(interceptor, levelValue)
                return interceptor as Interceptor
            }

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val networkAvailable = SjApplication.instance.isNetworkAvailable()
        val newRequest = chain.request()
        val cacheControlEnabled = newRequest.cacheControl.isPublic
        val isImageRequest = newRequest.header("Content-Type")
            ?.matches("image/.+".toRegex(RegexOption.IGNORE_CASE)) == true
        val finalRequest = newRequest.newBuilder()
            .url(newRequest.url.toString())
            .removeHeader("Pragma")
            .header("Connection", "Keep-Alive")
            .build()
        return when {
            //如果是图片内容，那么就一定先读取缓存，当缓存不存在时，再作下一步
            isImageRequest -> cacheResponse(finalRequest)
                ?: when {
                    networkAvailable ->
                        chain.proceed(finalRequest)
                    else -> networkUnavailableResponse()
                }
            else -> chain.proceed(finalRequest)
        }
    }

    private fun networkUnavailableResponse(): Response =
        Response.Builder().code(HttpResponseCode.HTTP_NETWORK_UNAVAILABLE).build()

    private fun cacheResponse(request: Request): Response? = cacheGet(
        request = request,
        cache = Cache(HttpApiManager.cacheDir, HttpApiManager.cacheTimeSeconds)
    )

}