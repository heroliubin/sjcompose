package com.cnbizmedia.comman.app

import com.cnbizmedia.comman.BuildConfig
import timber.log.Timber
import timber.log.Timber.*


/**
 * @Author: 曹秋淋
 * @Date: 2023/1/6
 * 统一日志输出类
 */
object LogUtils {

    private const val DefaultLogTAG = "CommonLog"

    /** 日志任务 **/
    private fun logIfEnabled(task: () -> Unit) {
        if (BuildConfig.DEBUG) task.invoke()
    }

    /**
     * info 级别日志输出
     * @param tag
     * @param msg 打印内容
     */
    fun log4i(
        tag: String,
        msg: String
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).i(msg)
    }

    /**
     * info 级别日志输出
     * @param tag
     * @param throwable 异常信息
     */
    fun log4i(
        tag: String,
        throwable: Throwable
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).i(throwable)
    }

    /**
     * desc 级别日志输出
     * @param tag
     * @param msg 打印内容
     */
    @JvmStatic
    fun log4d(
        tag: String,
        msg: String
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).d(msg)
    }

    /**
     * desc 级别日志输出
     * @param tag
     * @param throwable 异常信息
     */
    @JvmStatic
    fun log4d(
        tag: String,
        throwable: Throwable
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).d(throwable)
    }

    /**
     * waring 级别日志输出
     * @param tag
     * @param msg 打印内容
     */
    fun log4w(
        tag: String,
        msg: String?
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).w(msg)
    }

    /**
     * waring 级别日志输出
     * @param tag
     * @param msg 打印内容
     */
    fun log4w(
        tag: String,
        throwable: Throwable
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).w(throwable)
    }

    /**
     * error 级别日志输出
     * @param tag
     * @param msg 打印内容
     */
    fun log4e(
        tag: String,
        msg: String
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).e(msg)
    }

    /**
     * error 级别日志输出
     * @param tag
     * @param throwable 异常信息
     */
    fun log4e(
        tag: String,
        throwable: Throwable
    ) = logIfEnabled {
        if (Timber.treeCount == 0)
            Timber.plant(DebugTree())
        Timber.tag(tag).e(throwable)
    }
}