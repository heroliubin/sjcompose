package com.cnbizmedia.shangjie.api.core.interceptor

import com.cnbizmedia.comman.app.LogUtils
import com.cnbizmedia.network.core.manager.HttpApiManager

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val API_KEY = "gfedcba"
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_TIMESTAMP = "X-Sign-Timestamp"
        private const val HEADER_NONCE = "X-Sign-Nonce"
        private const val HEADER_APP_ID = "X-Sign-App-id"
        private const val HEADER_SIGNATURE = "X-Sign-Signature"
        private const val HEADER_SSID = "x-user-ssid"
        private const val SIGN_TYPE = "HmacSHA256"
        private val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val dft = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        dft.timeZone = TimeZone.getTimeZone("utc")
        val time = dft.format(Date())
        var nonce = ""
        for (i in 0..7)
            nonce += chars.random()
        val sign = getSign(request, time, nonce)
        val newRequest = request.newBuilder()
            .removeHeader(HEADER_USER_AGENT)
            .header(HEADER_TIMESTAMP, time)
            .header(HEADER_NONCE, nonce)
            .header(HEADER_SIGNATURE, sign)
            .header(HEADER_APP_ID, "android-app")
            .build()
        return chain.proceed(newRequest)
            .newBuilder()
            .run {// 因为接口有要求缓存，但是因为服务器返回的不给缓存，这里要做一个强制缓存
                if (!request.cacheControl.isPublic) this
                else removeHeader("Cache-Control")
                    .header("Cache-Control", HttpApiManager.cacheControlValue)
            }
            .build()
    }

    private fun getSign(
        request: Request,
        timestamp: String,
        nonce: String
    ): String {
        val httpUrl = request.url
        val nameSet = httpUrl.queryParameterNames
        val nameList = ArrayList<String>()
        nameList.addAll(nameSet)
        nameList.sortBy { it }
        val buffer = StringBuilder()
        for (i in 0 until nameList.size) {
            if (i != 0)
                buffer.append("&")
            buffer.append(nameList[i]).append("=").append(
                if (httpUrl.queryParameterValues(nameList[i]).isNotEmpty())
                    httpUrl.queryParameterValues(nameList[i])[0] else ""
            )
        }
        return hmacSha(getSHA256(request.method.uppercase() + httpUrl.encodedPath + buffer.toString()) + timestamp + nonce)
    }

    private fun getSHA256(str: String): String {
        var encodeStr = ""
        try {
            val messageDigest = MessageDigest.getInstance("SHA-256")
            messageDigest.update(str.toByteArray())
            encodeStr = byte2Hex(messageDigest.digest())
        } catch (e: Exception) {
            LogUtils.log4e("ApiKeyInterceptor", e)
        }
        return encodeStr
    }

    private fun hmacSha(VALUE: String): String {
        return try {
            val signingKey = SecretKeySpec(API_KEY.toByteArray(), SIGN_TYPE)
            val mac = Mac.getInstance(SIGN_TYPE)
            mac.init(signingKey)
            val rawHmac = mac.doFinal(VALUE.toByteArray())
            byte2Hex(rawHmac)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            return ""
        }
    }

    private fun byte2Hex(bytes: ByteArray): String {
        val stringBuffer = StringBuffer()
        for (i in bytes.indices) {
            stringBuffer.append(String.format("%02x", bytes[i]))
        }
        return stringBuffer.toString()
    }

}