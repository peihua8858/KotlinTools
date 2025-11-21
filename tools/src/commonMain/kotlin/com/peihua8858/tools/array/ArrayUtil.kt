package com.peihua8858.tools.array

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun <T> Array<T>?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && isNotEmpty() && isNotEmpty()
}

/**
 * 返回匹配给定 [predicate] 的第一个元素，如果未找到元素，则返回默认返回索引[0]元素。
 * @author dingpeihua
 * @date 2022/4/19 14:26
 * @version 1.0
 */
fun <T> Array<T>.findFirst(predicate: (T) -> Boolean): T {
    for (item in this) {
        if (predicate(item)) {
            return item
        }
    }
    return this[0]
}