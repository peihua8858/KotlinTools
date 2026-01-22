package com.peihua8858.tools.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isEmptyOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isEmptyOrBlank != null)
    }
    return isNullOrEmpty() || isNullOrBlank()
}

/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return !isNullOrEmpty() && isNotBlank()
}


/**
 * 验证文本是否是正整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isPositiveInteger(): Boolean {
    return isNonEmpty() && matches("^[1-9]d*$".toRegex())
}

/**
 * 验证文本是否是负整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isNegativeInteger(): Boolean {
    return isNonEmpty() && matches("^-[1-9]d*$".toRegex())
}
/**
 * 验证文本是否是浮点数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isFloatNumber(): Boolean {
    return this.isNonEmpty() && matches("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$".toRegex())
}

/**
 * 验证文本是否是双精度浮点数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isDoubleNumber(): Boolean {
    return this.isNonEmpty() && matches("^\\d{0,8}\\.{0,1}(\\d{1,2})?$".toRegex())
}

/**
 * 验证文本是否是数字
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isNumber(): Boolean {
    return isNonEmpty() && matches("^[0-9]*$".toRegex())
}

/**
 * 验证文本是否是整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isInteger(): Boolean {
    return isNonEmpty() && matches("^-?[1-9]d*$".toRegex())
}

/**
 * 验证是否包含数字
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isExistNumber(): Boolean {
    return isNonEmpty() && this.matches(".*\\d+.*".toRegex())
}

/**
 * 验证是否包含字母
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isExistChar(): Boolean {
    return isNonEmpty() && this.matches(".*[a-zA-Z]+.*".toRegex())
}

/**
 * 用户电话号码的打码隐藏加星号加*
 *
 * @return 处理完成的身份证
 */
fun CharSequence?.phoneMask(): String {
    var res = ""
    if (this.isNonEmpty()) {
        val stringBuilder = StringBuilder(this)
        res = stringBuilder.replaceRange(4, 9, "****").toString()
    }
    return res
}

const val MOBILE_PHONE =
    "^((\\+86)?(13\\d|14[5-9]|15[0-35-9]|16[5-6]|17[0-8]|18\\d|19[158-9])\\d{8})$"

fun CharSequence?.isPhoneNumber(): Boolean {
    return this != null && MOBILE_PHONE.toRegex().matches(this)
}


/**
 * 生成保护隐私的用户昵称
 * @param originNickname 原始用户昵称
 * @param defaultName 默认显示（如果originNickname参数为空，则默认返回改参数值）
 * @return a*****b 中间带了*号的昵称
 * @author dingpeihua
 * @date 2020/7/10 21:41
 * @version 1.0
 */
fun String?.generatePrivacyNickname(defaultName: String): String {
    if (this.isNullOrEmpty()) {
        return defaultName
    }
    return if (this.length == 1) {
        "$this*****"
    } else this[0] + "*****" +
            this[this.length - 1]
}

/**
 * 字符串拼接，
 * @param text 拼接字符串
 * @param separator 分隔符
 * @author dingpeihua
 * @date 2021/8/31 14:09
 * @version 1.0
 */
fun CharSequence?.joinToString(text: String?, separator: String = ","): CharSequence {
    if (this.isNonEmpty()) {
        if (text.isNonEmpty()) {
            return "$this$separator$text"
        }
        return this
    }
    return ""
}

/**
 * 删除最后一个字符
 * @author dingpeihua
 * @date 2022/1/12 15:00
 * @version 1.0
 */
fun StringBuilder.deleteEndChar(): StringBuilder {
    return deleteEndChar(",")
}

/**
 * 删除最后一个指定字符
 * @author dingpeihua
 * @date 2022/1/12 15:00
 * @version 1.0
 */
fun StringBuilder.deleteEndChar(endChar: String): StringBuilder {
    val index = lastIndexOf(endChar)
    if (isNotEmpty() && index == length - endChar.length) {
        removeRange(index, length)
    }
    return this
}


/**
 * 驼峰转下划线
 */
fun String.camelToSnakeCase(): String {
    return this.replace(Regex("([A-Z])"), "_$1").lowercase()
}

/**
 * 下划线转驼峰
 */
fun String.snakeToCamelCase(): String {
    return this.split("_").joinToString("") { replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
}

/**
 * 字符串截断
 */
fun String.ellipsis(maxLength: Int): String {
    return if (this.length >= maxLength) {
        this.substring(0, maxLength) + "..."
    } else {
        this
    }
}

fun String.textOverflow(maxLength: Int): String {
    return if (this.length >= maxLength) {
        this.substring(0, maxLength)
    } else {
        this
    }
}

inline fun <C : CharSequence> C?.ifNullOrEmpty(defaultValue: () -> C): C =
    if (isNullOrEmpty()) defaultValue() else this

inline fun <C : CharSequence> C?.ifNullOrBlank(defaultValue: () -> C): C =
    if (isNullOrBlank()) defaultValue() else this


@OptIn(ExperimentalContracts::class)
fun CharSequence?.ifEmptyOrBlank(defaultValue: () -> CharSequence): CharSequence {
    contract {
        returns(false) implies (this@ifEmptyOrBlank != null)
    }
    return this ?: defaultValue()
}