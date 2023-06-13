package com.cnbizmedia.comman.view

import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import com.cnbizmedia.comman.app.LogUtils
import com.cnbizmedia.comman.context.ContextProvider

/** Toast辅助类 **/
internal class ToastUtils private constructor() {

    companion object {

        private var mInstance: ToastUtils? = null

        @JvmStatic
        fun singleton(): ToastUtils {
            if (mInstance == null) {
                synchronized(ToastUtils::class.java) {
                    if (mInstance == null) {
                        mInstance = ToastUtils()
                    }
                }
            }
            return mInstance!!
        }

    }

    private var mToast: Toast? = null

    /**
     * 显示Toast提醒
     * @param message 消息内容
     * @param duration 显示时长，默认为：[Toast.LENGTH_SHORT]
     * @param gravity Toast的位置，默认底部居中
     */
    fun showToast(
        message: CharSequence?,
        duration: Int = Toast.LENGTH_SHORT,
        gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
    ) {
        try {
            if (message.isNullOrEmpty()) return
            dismissToast()
            mToast = Toast.makeText(
                ContextProvider.get().context, message, duration
            ).apply {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
                    setGravity(gravity, 0, 120.dip2px.toInt())
            }
            mToast?.show()
        } catch (e: Exception) {
            LogUtils.log4e("ToastUtils", e)
        }
    }

    /** 取消Toast显示 **/
    fun dismissToast() {
        if (mToast != null) {
            mToast?.cancel()
            mToast = null
        }
    }

}

/**
 * 显示Toast信息
 * @param message 消息内容，如果是NULL或者空，则不给与显示
 * @param duration 显示时长
 * @param gravity 默认的位置, 高版本安卓不生效
 */
fun showToast(
    message: CharSequence?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
) = ToastUtils.singleton().showToast(message, duration, gravity)

/**
 * 显示Toast信息
 * @param messageId 消息内容Id
 * @param duration 显示时长
 * @param gravity 默认的位置, 高版本安卓不生效
 */
fun showToast(
    @StringRes messageId: Int,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
) = showToast(ContextProvider.get().context?.getString(messageId), duration, gravity)