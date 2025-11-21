@file:JvmName("CollectionsUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.collections

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

@OptIn(ExperimentalContracts::class)
fun <T> Collection<T>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && isNotEmpty() && isNotEmpty()
}

fun <T> Collection<T>?.toArrayList(): ArrayList<T> {
    if (this == null) {
        return arrayListOf()
    }
    return ArrayList(this)
}

/**
 * 返回匹配给定 [predicate] 的第一个元素，如果未找到元素，则返回默认返回索引[0]元素。
 * @author dingpeihua
 * @date 2022/4/19 14:26
 * @version 1.0
 */
fun <T> MutableList<T>.findOrFirst(predicate: (T) -> Boolean): T {
    for (item in this) {
        if (predicate(item)) {
            return item
        }
    }
    return this[0]
}

fun <T, R> List<T>.convert(predicate: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(predicate(item))
    }
    return result
}

fun <T> MutableList<T>.removeBy(predicate: (T) -> Boolean): List<T> {
    var i = 0
    while (i < this.size) {
        if (predicate(this[i])) {
            removeAt(i)
            continue
        }
        i++
    }
    return this
}

fun <T> MutableList<T>.filterData(predicate: (T) -> Boolean): MutableList<T> {
    var i = 0
    while (i < this.size) {
        if (predicate(this[i])) {
            removeAt(i)
            continue
        }
        i++
    }
    return this
}


/**
 * 返回匹配给定 [predicate] 的第一个元素，如果没有找到这样的元素，则返回 `null`。
 * @param <T>       泛型参数，集合中放置的元素数据类型
 * @param predicate 给定条件操作符
 * @return 如果集合不为空返回输出字符串，否则返回"null"
 */
inline fun <K, V> Map<K, V>.findValue(predicate: (Map.Entry<K, V>) -> Boolean): V? {
    for (item in this) {
        if (predicate(item)) {
            return item.value
        }
    }
    return null
}

fun String.toLongList(split: String = ","): MutableList<Long> {
    return this.split(split).map { it.toLong() }.toMutableList()
}

fun String.toIntList(split: String = ","): MutableList<Int> {
    return this.split(split).map { it.toInt() }.toMutableList()
}

fun String.toFloatList(split: String = ","): MutableList<Float> {
    return this.split(split).map { it.toFloat() }.toMutableList()
}

fun String.toDoubleList(split: String = ","): MutableList<Double> {
    return this.split(split).map { it.toDouble() }.toMutableList()
}

fun String.toLongArray(split: String = ","): LongArray {
    return this.split(split).map { it.toLong() }.toLongArray()
}

fun String.toIntArray(split: String = ","): IntArray {
    return this.split(split).map { it.toInt() }.toIntArray()
}

fun String.toFloatArray(split: String = ","): FloatArray {
    return this.split(split).map { it.toFloat() }.toFloatArray()
}

fun String.toDoubleArray(split: String = ","): DoubleArray {
    return this.split(split).map { it.toDouble() }.toDoubleArray()
}