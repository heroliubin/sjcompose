package com.cnbizmedia.shangjie.api.core

import com.cnbizmedia.comman.app.LogUtils
import com.cnbizmedia.network.core.manager.HttpApiManager
import com.cnbizmedia.network.core.manager.HttpApiManagerConfig
import com.cnbizmedia.shangjie.BuildConfig
import com.cnbizmedia.shangjie.api.core.interceptor.ApiKeyInterceptor
import com.cnbizmedia.shangjie.api.core.interceptor.GlobalInterceptor
import com.cnbizmedia.shangjie.api.service.AppApiService
import com.cnbizmedia.shangjie.api.url.ApiUrls
import okhttp3.Interceptor

/** 网络请求管理类 **/
class ApiManager private constructor() : HttpApiManager(defaultHttpManagerConfig) {

    companion object {

        /** 获取默认的网络管理配置对象 **/
        val defaultHttpManagerConfig: HttpApiManagerConfig
            get() = HttpApiManagerConfig(
                noProxy = !BuildConfig.DEBUG,
                baseUrl = ApiUrls.BaseUrl,
                httpInterceptors = mutableListOf<Interceptor>().apply {
                    try {
                        if (BuildConfig.DEBUG)
                            add(GlobalInterceptor.httpLoggingInterceptor)
                    } catch (e: Exception) {
                        LogUtils.log4i("ApiManager", e)
                    } finally {
                        add(ApiKeyInterceptor())
                    }
                },
                interceptors = listOf(GlobalInterceptor()),
            )

        private val mInstance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiManager() }

        /** 获取单例对象 **/
        @JvmStatic
        fun singleton(): ApiManager = mInstance

        /** 获取应用接口服务 **/
        val appService: AppApiService
            get() = singleton().getService(AppApiService::class)


    }

}