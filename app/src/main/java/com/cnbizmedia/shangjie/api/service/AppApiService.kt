package com.cnbizmedia.shangjie.api.service

import com.cnbizmedia.network.core.manager.HttpApiManager
import com.cnbizmedia.shangjie.api.resp.ADData
import com.cnbizmedia.shangjie.api.resp.ApiResponseInfo
import okhttp3.RequestBody
import retrofit2.http.*

/** 应用API服务 **/
interface AppApiService {

    /** 获取开屏广告信息 **/
    @GET("https://adm.kanshangjie.com/v1/List?appId=7&appSecret=93377492c0ecc88b")
    @Headers(HttpApiManager.cacheControlHeader)
    suspend fun getAD(): ApiResponseInfo<ADData>

}