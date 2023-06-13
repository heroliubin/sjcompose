package com.cnbizmedia.comman.app

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.cnbizmedia.comman.context.ContextProvider
import java.util.*

/** 设备辅助方法 **/
object DeviceUtils {

    /** 获取设备ID **/
    @JvmStatic
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        val deviceId: String
        var androidId: String? = null
        try {
            androidId = Settings.Secure.getString(
                ContextProvider.get().context?.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } finally {
            if (androidId.isNullOrEmpty() || "9774d56d682e549c" == androidId)
                androidId = UUID.randomUUID().toString()
            val deviceInfo =
                "$androidId-${Build.HARDWARE}-${Build.BRAND}-${Build.MANUFACTURER}-${Build.PRODUCT}-${Build.FINGERPRINT}-${Build.HOST}-${Build.DISPLAY}"
            deviceId = UUID.nameUUIDFromBytes(deviceInfo.toByteArray()).toString()
        }
        return deviceId
    }

}