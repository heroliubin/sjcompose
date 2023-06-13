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


    /** 是否成功 **/
    val isSuccessful: Boolean

}

