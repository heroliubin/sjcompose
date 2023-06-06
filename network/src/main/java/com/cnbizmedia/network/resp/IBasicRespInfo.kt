package com.cnbizmedia.network.resp

import com.google.gson.annotations.SerializedName

/** Response输出模型 **/
interface IBasicRespInfo<T> {

    /** 结果码 **/
    var code: String

    /** 输出信息 **/
    var message: String?

    /** 输出结果 **/
    var data: T?

    /** 分页标识 **/
    var pagination: Pagination?

    /** 时间戳 **/
    var timestamp: String?

    /** 报错信息 **/
    var errors: Any?

    /** 是否成功 **/
    val isSuccessful: Boolean

}

/** 分页实体类 **/
data class Pagination(
    @field:SerializedName("current")
    val current: Int = 0,
    @field:SerializedName("next")
    val next: Int = 0
)