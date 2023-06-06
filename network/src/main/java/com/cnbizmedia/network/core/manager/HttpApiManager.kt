@file: JvmName("HttpApiManager")

package com.cnbizmedia.network.core.manager

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.cnbizmedia.network.BuildConfig
import com.cnbizmedia.network.converter.JsonConverterFactory
import com.cnbizmedia.network.converter.NoResponseBodyConverterFactory
import com.cnbizmedia.network.converter.NullOrEmptyConverterFactory
import com.cnbizmedia.network.provider.ContextProvider
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.net.Proxy
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass

/**
 * HttpApi管理类
 * @param config API网络请求配置内容
 * @param retrofit Retrofit实例
 */
abstract class HttpApiManager(
    config: HttpApiManagerConfig,
    private val retrofit: Retrofit = getDefaultRetrofit(config)
) {

    /** API缓存容器 **/
    private val httpApiCache = HashMap<KClass<*>, Any?>()

    /**
     * 获取Api接口服务
     *
     * @param clazz API接口定义
     * @param <T>   API接口定义的泛型类
     **/
    fun <T : Any> getService(clazz: KClass<T>): T {
        if (!httpApiCache.containsKey(clazz)) {
            val t = retrofit.create(clazz.java)
            httpApiCache[clazz] = t
            return t
        }
        @Suppress("UNCHECKED_CAST")
        return httpApiCache[clazz] as T
    }

    companion object {

        /** 网络缓存的默认时间：秒 **/
        const val cacheTimeSeconds: Long = 30 * 24 * 60 * 60L

        /** 网络缓存Header取值 **/
        const val cacheControlValue = "public, max-age=$cacheTimeSeconds"

        /** 网络缓存Header **/
        const val cacheControlHeader = "Cache-Control: $cacheControlValue"

        /** 网络缓存的目录 **/
        val cacheDir: File
            get() = File(ContextProvider.get().context?.cacheDir?.absolutePath?.plus("/network/cache")!!)

        /** 获取默认的GSON配置 **/
        private val defaultGson: Gson
            get() = GsonBuilder().serializeNulls().setLenient().create()

        /**
         * 获取默认的OkHttpClient
         * @param config 网络API管理器的相关配置
         */
        fun getDefaultRetrofit(config: HttpApiManagerConfig): Retrofit {
            val retrofit = Retrofit.Builder()
            return retrofit.apply {
                baseUrl(config.baseUrl)
                addConverterFactory(JsonConverterFactory.create(defaultGson))
                addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                addConverterFactory(NoResponseBodyConverterFactory.create())
                addConverterFactory(NullOrEmptyConverterFactory.create())
                client(getDefaultOkHttpClient(config))
            }.build()
        }

        /**
         * 获取默认配置好OkHttpClient
         * @param config 网络管理器的配置
         */
        fun getDefaultOkHttpClient(config: HttpApiManagerConfig): OkHttpClient {
            val (trustManager, sslSocketFactory) = try {
                val trustManagers =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                        .apply {
                            @Suppress("CAST_NEVER_SUCCEEDS")
                            init(null as? KeyStore)
                        }.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)
                    throw IllegalStateException(
                        "Unexpected default trust managers: ${Arrays.toString(trustManagers)}"
                    )
                val trustManager = trustManagers.first() as X509TrustManager
                val sslSocketFactory = SSLContext.getInstance("TLS")
                    .apply { init(null, arrayOf(trustManager), null) }
                    .socketFactory
                trustManager to sslSocketFactory
            } catch (e: Exception) {
                if (BuildConfig.DEBUG)
                    Log.e("HTTP_API_MANAGER", e.message ?: "错误")
                Pair(null, null)
            }
            return OkHttpClient.Builder().apply {
                if (!BuildConfig.DEBUG && config.noProxy)
                    proxy(Proxy.NO_PROXY)
                connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
                writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
                readTimeout(config.readTimeout, TimeUnit.SECONDS)
                if (sslSocketFactory != null && trustManager != null)
                    sslSocketFactory(sslSocketFactory, trustManager)
                cache(Cache(cacheDir, cacheTimeSeconds)) //设置网络缓存
                config.interceptors.forEach { addInterceptor(it) }
                config.httpInterceptors.forEach { addNetworkInterceptor(it) }
                connectionPool(ConnectionPool(6, 2, TimeUnit.MINUTES))
//                //DEBUG模式下添加 HttpLoggingInterceptor, 用于输出请求的消息内容
//                if (BuildConfig.DEBUG) applyHttpLoggingInterceptor()
            }.build()
        }

//        /** 应用API网络日志Interceptor **/
//        private fun OkHttpClient.Builder.applyHttpLoggingInterceptor() {
//            try {
//                val clazz =
//                    Class.forName("okhttp3.logging.HttpLoggingInterceptor")
//                val loggingInterceptorInstance = clazz.newInstance()
//                val addInterceptorMethod =
//                    javaClass.getMethod("addInterceptor", Interceptor::class.java)
//                addInterceptorMethod.invoke(this, loggingInterceptorInstance)
//                val levelEnum =
//                    Class.forName("okhttp3.logging.HttpLoggingInterceptor\$Level")
//                val bodyField = levelEnum.getDeclaredField("BODY")
//                clazz.getMethod(
//                    "setLevel",
//                    Class.forName("okhttp3.logging.HttpLoggingInterceptor\$Level")
//                ).invoke(
//                    loggingInterceptorInstance,
//                    bodyField[null]
//                )
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

    }
}