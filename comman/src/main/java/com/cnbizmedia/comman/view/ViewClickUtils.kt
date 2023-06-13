package com.cnbizmedia.comman.view

import android.os.SystemClock
import android.view.View
import kotlin.math.abs

/******************************************************
 *                                                    *
 *                控件单击事件的处理                     *
 *                                                    *
 * ****************************************************
 */

/**
 * 设置控件的单击事件
 * @param timeSpan 每次点击的相应间隔时间，默认为：800毫秒
 * @param onClick 点击事件
 */
fun View.setOnSingleClickListener(
    timeSpan: Long = 800L,
    onClick: View.() -> Unit
) = setOnClickListener(OnViewSingleTapClickListener({ onClick(it) }, timeSpan))

/**
 * 设置控件的单击事件
 *
 * @param listener 点击事件监听器
 * @param timeSpan 每次点击的相应间隔时间，默认为：800毫秒
 */
fun View.setOnSingleClickListener(
    listener: View.OnClickListener,
    timeSpan: Long = 800L
) = setOnClickListener(OnViewSingleTapClickListener(listener, timeSpan))

/** 一次性点击事件辅助类 **/
private class OnViewSingleTapClickListener(
    private val listener: View.OnClickListener,
    private val timeSpan: Long = 800L
) : View.OnClickListener {

    /** 最近一次点击的时间 **/
    private var lastClickTime: Long = 0

    /**
     * 是否是快速点击
     *
     * @param timeSpan  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    private fun isFastDoubleClick(timeSpan: Long): Boolean =
        SystemClock.elapsedRealtime().run {
            return if (abs(this - lastClickTime) < timeSpan) {
                true
            } else {
                lastClickTime = this
                false
            }
        }

    override fun onClick(v: View?) {
        if (!isFastDoubleClick(timeSpan)) listener.onClick(v)
    }

}

/******************************************************
 *                                                    *
 *                控件双击事件的处理                     *
 *                                                    *
 * ****************************************************
 */

/**
 * 设置控件的双击事件
 * @param timeSpan 每次点击的相应间隔时间，默认为：600毫秒
 * @param onClick 点击事件
 */
fun View.setOnDoubleClickListener(timeSpan: Long = 600, onClick: View.() -> Unit) =
    setOnClickListener(OnViewDoubleTapClickListener({ onClick(it) }, timeSpan))

/**
 * 设置控件的双击事件
 *
 * @param listener 点击事件监听器
 * @param timeSpan 每次点击的相应间隔时间，默认为：600毫秒内的点击就判定为双击
 */
fun View.setOnDoubleClickListener(listener: View.OnClickListener, timeSpan: Long = 600) =
    setOnClickListener(OnViewDoubleTapClickListener(listener, timeSpan))

/** 双击事件辅助类 **/
private class OnViewDoubleTapClickListener(
    private val listener: View.OnClickListener,
    private val timeSpan: Long = 600
) : View.OnClickListener {

    /** 最近一次点击的时间 **/
    private var lastClickTime: Long = 0

    /**
     * 是否是快速点击
     *
     * @param timeSpan  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    private fun isFastDoubleClick(timeSpan: Long): Boolean =
        SystemClock.elapsedRealtime().run {
            return if (abs(this - lastClickTime) < timeSpan) {
                true
            } else {
                lastClickTime = this
                false
            }
        }

    override fun onClick(v: View?) {
        if (isFastDoubleClick(timeSpan)) {
            lastClickTime = 0
            listener.onClick(v)
        }
    }

}