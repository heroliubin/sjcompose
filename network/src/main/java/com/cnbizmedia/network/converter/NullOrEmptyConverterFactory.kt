package com.cnbizmedia.network.converter

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/** 处理Body为空的状态转换器 **/
internal class NullOrEmptyConverterFactory private constructor() : Converter.Factory() {

    companion object {

        @JvmStatic
        fun create() = NullOrEmptyConverterFactory()

    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> = Converter<ResponseBody, Any> { body ->
        if (body.contentLength() == 0L) null
        else retrofit.nextResponseBodyConverter<Any>(this, type, annotations).convert(body)
    }

}