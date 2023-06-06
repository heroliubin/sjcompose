package com.cnbizmedia.network.service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Streaming
import retrofit2.http.Url

interface HttpApiService {

    /**
     * 文件下载服务
     * @param url 下载链接
     * @param headers 请求头
     */
    @GET
    @Streaming
    suspend fun downloadFile(
        @Url url: String,
        @HeaderMap headers: Map<String, String> = mapOf()
    ): ResponseBody

    /**
     * 文件下载服务
     * @param url 下载链接
     * @param headers 请求头
     */
    @GET
    @Streaming
    fun downloadFile2(
        @Url url: String,
        @HeaderMap headers: Map<String, String> = mapOf()
    ): Call<ResponseBody>
}