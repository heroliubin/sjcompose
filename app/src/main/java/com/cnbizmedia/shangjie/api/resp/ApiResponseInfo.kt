package com.cnbizmedia.shangjie.api.resp

import com.google.gson.annotations.SerializedName
import com.cnbizmedia.network.resp.IBasicRespInfo


/**
 * API返回请求基层数据结构类
 * @param code 请求结果code
 * @param message 返回消息
 * @param data 数据内容
 */
data class ApiResponseInfo<T>(
    @field: SerializedName("code")
    override var code: String,
    @field: SerializedName("message")
    override var message: String?,
    @field: SerializedName("data")
    override var data: T?
) : IBasicRespInfo<T> {
    override val isSuccessful: Boolean
        get() = code == "1"

}
