package com.cnbizmedia.comman.base

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 替换集合中的所有元素
 * @param elements 新元素集合
 */
inline fun <reified T> MutableList<T>.replaceWith(elements: Collection<T>) {
    clear(); addAll(elements)
}

/**
 * 替换集合中的所有元素
 * @param elements 新元素集合
 */
inline fun <reified T> MutableList<T>.replaceWith(vararg elements: T) {
    clear(); addAll(elements)
}

/**
 * 替换集合中的所有元素
 * @param elements 新元素集合
 */
inline fun <reified T> ConcurrentLinkedQueue<T>.replaceWith(vararg elements: T) {
    clear(); addAll(elements)
}

/**
 * 替换集合中的所有元素
 * @param elements 新元素集合
 */
inline fun <reified T> ConcurrentLinkedQueue<T>.replaceWith(elements: Collection<T>) {
    clear(); addAll(elements)
}