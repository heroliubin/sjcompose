package com.cnbizmedia.network.converter

import com.cnbizmedia.network.BuildConfig
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.Charset

internal class JsonConverterFactory private constructor(private val gson: Gson) :
    Converter.Factory() {

    companion object {

        @JvmStatic
        fun create(gson: Gson): JsonConverterFactory = JsonConverterFactory(gson)

    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> =
        JsonResponseBodyConverter(gson, gson.getAdapter(TypeToken.get(type)))

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> =
        JsonRequestBodyConverter(gson, gson.getAdapter(TypeToken.get(type)))

}

private class JsonRequestBodyConverter<T>(private val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<T, RequestBody> {

    companion object {

        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()

        private val UTF_8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        with(gson.newJsonWriter(writer)) {
            adapter.write(this, value)
            close()
        }
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }

}

private class JsonResponseBodyConverter<T>(private val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T {
        try {
            val jsonReader = gson.newJsonReader(value.charStream())
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT)
                throw JsonIOException("JSON document was not fully consumed.")
            return result
        } catch (e: Exception) {
            if (BuildConfig.DEBUG)
                println("o(╥﹏╥)o 解析数据(JSON)错误： $e")
            throw JsonIOException("JSON 解析错误: ${e.message}")
        }
    }

}