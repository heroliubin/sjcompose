package com.cnbizmedia.network.provider

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/** 提供Context实例的类 **/
internal class ContextProvider private constructor(context: Context?) {

    private val mContext: Context? = context

    /** 获取上下文 **/
    val context: Context?
        get() = mContext

    /** application实例 **/
    val application: Application?
        get() = mContext?.applicationContext as? Application

    companion object {

        @Volatile
        @SuppressLint("StaticFieldLeak")
        private var instance: ContextProvider? = null

        /** 获取实例 **/
        @JvmStatic
        fun get(): ContextProvider {
            if (instance == null) {
                synchronized(ContextProvider::class.java) {
                    if (instance == null) {
                        val context: Context = AppContextProvider.mContext
                            ?: throw IllegalStateException("context == null")
                        instance = ContextProvider(context)
                    }
                }
            }
            return instance!!
        }

        /** 提供Context对象 **/
        fun provideContext(): Context? = get().context

        /** 提供Application对象 **/
        fun provideApplication(): Application? = get().application

    }

}