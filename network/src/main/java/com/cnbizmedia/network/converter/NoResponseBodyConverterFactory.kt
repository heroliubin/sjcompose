package com.cnbizmedia.network.converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/** 代替gson-converter转换无响应体的response **/
internal class NoResponseBodyConverterFactory private constructor() : Converter.Factory() {

    class NoResponseBodyEntity

    companion object {

        @JvmStatic
        fun create(): NoResponseBodyConverterFactory = NoResponseBodyConverterFactory()

    }

    //将响应对象responseBody转成目标类型对象(也就是Call里给定的类型)
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        //判断当前的类型是否是我们需要处理的类型
        return if (NoResponseBodyEntity::class.java == type) {
            //是则创建一个Converter返回转换数据
            Converter<ResponseBody, NoResponseBodyEntity> {
                //这里直接返回null是因为我们不需要使用到响应体,本来也没有响应体.
                //返回的对象会存到response.body()里.
                null
            }
        } else null
    }

    //其它两个方法我们不需要使用到.所以不需要重写.
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? = null

    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? = null

}