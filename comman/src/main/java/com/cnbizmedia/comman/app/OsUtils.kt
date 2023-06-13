package com.cnbizmedia.comman.app

/** 操作系统辅助类 **/
object OsUtils {

    /**
     * 判断是否是鸿蒙操作系统
     * @return 是否是鸿蒙操作系统
     */
    @JvmStatic
    fun isHarmonyOs(): Boolean = try {
        @Suppress("SENSELESS_COMPARISON")
        Class.forName("ohos.utils.system.SystemCapability") != null
    } catch (e: Exception) {
        LogUtils.log4e("AppUtils[isHarmonyOs]", e)
        false
    }

    /**
     * 获取鸿蒙系统 Version
     * @return HarmonyOS Version
     */
    @JvmStatic
    fun getHarmonyOSVersion(): String? {
        var version: String? = null
        if (isHarmonyOs())
            version = System.getProperty("hw_sc.build.platform.version", "")
        return version
    }

}