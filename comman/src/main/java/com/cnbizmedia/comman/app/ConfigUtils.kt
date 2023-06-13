@file: Suppress("IMPLICIT_CAST_TO_ANY")

package com.cnbizmedia.comman.app

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.reflect.KClass

/**
 * 配置工具类
 * @param mmkv MMKV对象，默认使用Default的MMKV对象
 */
class ConfigUtils constructor(private val mmkv: MMKV = MMKV.defaultMMKV()) {

    companion object {

        /** 初始化方法, 在Application中调用 **/
        @JvmStatic
        fun initialize(context: Context): String = MMKV.initialize(context)

        /** 获取ConfigUtils的单例对象 **/
        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ConfigUtils() }

        /** 获取默认的配置对象 **/
        @JvmStatic
        fun default(): ConfigUtils = instance

    }

    /**
     * 读取值
     * @param key 键值
     * @param default 默认值
     */
    @Suppress("unchecked_cast")
    fun <T> read(key: String, default: T): T {
        return when (default) {
            is Boolean ->
                mmkv.decodeBool(key, default)
            is Int ->
                mmkv.decodeInt(key, default)
            is Long ->
                mmkv.decodeLong(key, default)
            is Float ->
                mmkv.decodeFloat(key, default)
            is Double ->
                mmkv.decodeDouble(key, default)
            is String ->
                mmkv.decodeString(key, default) ?: default
            is ByteArray ->
                mmkv.decodeBytes(key, default) ?: default
            is Set<*> ->
                mmkv.decodeStringSet(key, default.map { it.toString() }.toSet()) ?: default
            is Parcelable ->
                mmkv.decodeParcelable(key, Parcelable::class.java) ?: default
            else -> throw Throwable("不支持类型：TYPE Of $default")
        } as T
    }

    /**
     * 写入值
     * @param key 键值
     * @param value 需要写入的值
     */
    fun <T> write(key: String, value: T): Boolean {
        return when (value) {
            is Boolean ->
                mmkv.encode(key, value)
            is Int ->
                mmkv.encode(key, value)
            is Long ->
                mmkv.encode(key, value)
            is Float ->
                mmkv.encode(key, value)
            is Double ->
                mmkv.encode(key, value)
            is String ->
                mmkv.encode(key, value)
            is ByteArray ->
                mmkv.encode(key, value)
            is Parcelable ->
                mmkv.encode(key, value)
            is Set<*> ->
                mmkv.encode(key, value.map { v -> v.toString() }.toSet())
            else -> throw Throwable("不支持类型：TYPE Of $value")
        }
    }

    /**
     * 读取Parcelable数据
     * @param key 存储Key
     * @param clazz Parcelable的类
     */
    fun <T : Parcelable> readParcelable(key: String, clazz: KClass<T>): T? =
        mmkv.decodeParcelable(key, clazz.java)

    /**
     * 读取StringSet数据
     * @param key 存储Key
     */
    fun readStringSet(key: String): MutableSet<String>? = mmkv.decodeStringSet(key)

    /**
     * 是否包含某个键值
     * @param key 键值
     */
    fun contains(key: String) = mmkv.contains(key)

    /**
     * 删除某个键值的数据
     * @param key 键值
     */
    fun remove(key: String) = mmkv.removeValueForKey(key)

    /**
     * 删除多个键值的数据
     * @param keys 键值集合
     */
    fun removeKeys(vararg keys: String) = mmkv.removeValuesForKeys(keys)

    /** 清空所有数据 **/
    fun clear() = mmkv.clearAll()

}