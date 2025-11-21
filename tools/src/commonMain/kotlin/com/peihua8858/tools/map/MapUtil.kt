package com.peihua8858.tools.map

import kotlinx.coroutines.joinAll
import kotlin.collections.joinTo
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <K, V> Map<K, V>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && size > 0 && isNotEmpty()
}

/**
 * map<K,Collection<V> 浅拷贝
 * @author dingpeihua
 * @date 2021/2/26 11:00
 * @version 1.0
 */
fun <K, V> Map<K, MutableList<V>>?.copyOfMapList(): MutableMap<K, MutableList<V>> {
    val cloneMap = mutableMapOf<K, MutableList<V>>()
    if (this.isNullOrEmpty()) {
        return cloneMap
    }
    val it = this.iterator()
    while (it.hasNext()) {
        val entry = it.next()
        cloneMap[entry.key] = ArrayList(entry.value)
    }
    return cloneMap
}


/**
 * 返回匹配给定 [predicate] 的第一个元素，如果没有找到这样的元素，则返回 `null`。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param predicate 给定条件操作符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V> Map<K, V>.findKey(predicate: (Map.Entry<K, V>) -> Boolean): K? {
    for (item in this) {
        if (predicate(item)) {
            return item.key
        }
    }
    return null
}

/**
 * 返回匹配给定 [predicate] 的所有元素列表，如果没有找到这样的元素，则返回空列表。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param predicate 给定条件操作符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V> Map<K, V>.findKeys(predicate: (Map.Entry<K, V>) -> Boolean): MutableList<K> {
    return findTo(mutableListOf(), predicate)
}

/**
 * 返回匹配给定 [predicate] 的所有元素列表，如果没有找到这样的元素，则返回空列表。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param destination 目标列表
 * @param predicate 给定条件操作符
 * @author dingpeihua
 * @date 2022/4/11 10:20
 * @version 1.0
 */
inline fun <K, V, M : MutableList<in K>> Map<out K, V>.findTo(
    destination: M,
    predicate: (Map.Entry<K, V>) -> Boolean
): M {
    for (element in this) {
        if (predicate(element)) {
            destination.add(element.key)
        }
    }
    return destination
}

/**
 * 集合转成String输出
 *
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param separator 分隔符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
public fun <K, V, R> Map<K, V>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Map.Entry<K, V>) -> CharSequence)? = null
): String {
    return joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
}

/**
 * Appends the string from all the elements separated using [separator] and using the given [prefix] and [postfix] if supplied.
 *
 * If the collection could be huge, you can specify a non-negative value of [limit], in which case only the first [limit]
 * elements will be appended, followed by the [truncated] string (which defaults to "...").
 *
 */
public fun <K, V, A : Appendable> Map<K, V>.joinTo(
    buffer: A,
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((Map.Entry<K, V>) -> CharSequence)? = null
): A {
    buffer.append(prefix)
    var count = 0
    for (element in this.entries) {
        if (++count > 1) buffer.append(separator)
        if (limit !in 0..<count) {
            buffer.appendElement(element, transform)
        } else break
    }
    if (limit in 0..<count) buffer.append(truncated)
    buffer.append(postfix)
    return buffer
}

fun <K, V, T> Map<K, V>.convert(p: (Map.Entry<K, V>) -> Map.Entry<K, T>): Map<K, T> {
    val result = mutableMapOf<K, T>()
    for (item in this) {
        val r = p(item)
        result[r.key] = r.value
    }
    return result
}

internal fun <T> Appendable.appendElement(element: T, transform: ((T) -> CharSequence)?) {
    when {
        transform != null -> append(transform(element))
        element is CharSequence? -> append(element)
        element is Char -> append(element)
        else -> append(element.toString())
    }
}