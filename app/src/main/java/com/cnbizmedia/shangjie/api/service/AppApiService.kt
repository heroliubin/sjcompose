package com.cnbizmedia.shangjie.api.service

import com.cnbizmedia.network.core.manager.HttpApiManager
import com.cnbizmedia.shangjie.api.resp.ADData
import com.cnbizmedia.shangjie.api.resp.ApiResponseInfo
import com.cnbizmedia.shangjie.api.resp.Category
import com.cnbizmedia.shangjie.api.resp.HotNews
import com.cnbizmedia.shangjie.api.resp.SjNews
import okhttp3.RequestBody
import retrofit2.http.*

/** 应用API服务 **/
interface AppApiService {

    /** 获取开屏广告信息 **/
    @GET("https://adm.kanshangjie.com/v1/List?appId=7&appSecret=93377492c0ecc88b")
    @Headers(HttpApiManager.cacheControlHeader)
    suspend fun getAD(): ApiResponseInfo<ADData>

    /** 栏目分类 **/
    @GET("Category/Index")
    @Headers(HttpApiManager.cacheControlHeader)
    suspend fun getCategory(): ApiResponseInfo<List<Category>>

    /** 精选新闻 **/
    @GET("Category/RecommendNews")
    @Headers(HttpApiManager.cacheControlHeader)
    suspend fun getRecommendNews(
        @Query("p") p: Int,
        @Query("ps") ps: Int,
        @Query("top") top: Int,
        @Query("slug") slug: String
    ): ApiResponseInfo<SjNews>

    /** 热点新闻 **/
    @GET("Category/HotNewsDay")
    @Headers(HttpApiManager.cacheControlHeader)
    suspend fun getHotNews(): ApiResponseInfo<List<HotNews>>
}