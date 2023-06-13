package com.cnbizmedia.comman.gson

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.cnbizmedia.comman.app.LogUtils


/** json字符串转object **/
inline fun <reified T> String.toJsonObject(): T? {
    return try {
        if (trim().isEmpty()) null
    else Gson().apply { serializeNulls() }
            .fromJson<T>(this, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        LogUtils.log4e("GsonUtils", e)
        null
    }
}

/** object转JSON字符串 **/
fun Any.toJsonString(): String {
    return try {
        Gson().apply { serializeNulls() }.toJson(this)
    } catch (e: java.lang.Exception) {
        LogUtils.log4e("GsonUtils", e)
        ""
    }
}
