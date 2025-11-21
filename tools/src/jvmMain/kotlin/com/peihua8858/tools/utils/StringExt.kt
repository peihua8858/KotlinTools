@file:JvmName("StringUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.utils


import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.io.encoding.Base64

fun String?.toBase64(): ByteArray? {
    return if (null == this) {
        null
    } else Base64.encodeToByteArray(toByteArray())
}

fun String?.toBase64ToString(): String {
    return if (null == this) {
        ""
    } else Base64.encode(toByteArray())
}

private val sf = SimpleDateFormat("yyyy-MM-dd")

/**
 * 根据时间戳创建文件名
 *
 * @return
 */
fun String.createFolderFileName(): String {
    val millis = System.currentTimeMillis()
    return this + sf.format(millis)
}
private const val IP_ADDRESS_STRING = ("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
        + "|[1-9][0-9]|[0-9]))")
val IP_ADDRESS = Pattern.compile(IP_ADDRESS_STRING)
/**
 * 验证是否是IP地址
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isIpAddress(): Boolean {
    return !this.isNullOrEmpty() && IP_ADDRESS.matcher(this).matches()
}
val EMAIL_ADDRESS = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
/**
 * 验证是否是邮箱
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isEmail(): Boolean {
    return this != null && EMAIL_ADDRESS.matcher(this).matches()
}


fun conValidate(con: CharSequence?): Boolean {
    if (null != con && "" != con) {
        if ((con.isChinese() || con.matches(Regex("^[A-Za-z]+$")))
            && con.length <= 10
        ) {
            return true
        }
    }
    return false
}

/**
 * 判断当前字符串是不是包含中文
 * @author dingpeihua
 * @date 2020/11/26 9:10
 * @version 1.0
 */
fun CharSequence?.isChinese(): Boolean {
    if (this == null) {
        return false
    }
    val pattern = Pattern.compile("[\\u4e00-\\u9fa5]")
    val matcher: Matcher = pattern.matcher(this)
    return matcher.find()
}

/**
 * 编码中文
 *
 * @param url
 * @author dingpeihua
 * @date 2019/7/6 18:47
 * @version 1.0
 */
fun CharSequence.encodeChinese(): CharSequence {
    var tempUrl = this
    try {
        val matcher: Matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(tempUrl)
        while (matcher.find()) {
            val tmp: String = matcher.group()
            tempUrl = tempUrl.replace(tmp.toRegex(), URLEncoder.encode(tmp, "UTF-8"))
        }
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return tempUrl
}

/**
 * 首字母大写
 *
 * @return 成功返回true，失败返回false
 */
fun CharSequence?.firstLetterUpperCase(local: Locale): String {
    if (this.isEmptyOrBlank()) {
        return ""
    }
    if (length <= 1) {
        return this.toString().uppercase(local)
    }
    val firstLetter = substring(0, 1).uppercase(local)
    return firstLetter + substring(1)
}

/**
 * 首字母大写
 *
 * @return 成功返回true，失败返回false
 */
fun CharSequence?.firstLetterUpperCase(): String {
    return firstLetterUpperCase(Locale.ROOT)
}
//
///**
// * 删除最后一个字符
// * @author dingpeihua
// * @date 2022/1/12 15:00
// * @version 1.0
// */
//fun StringBuilder.deleteEndChar(): StringBuilder {
//    return deleteEndChar(",")
//}
//
///**
// * 删除最后一个指定字符
// * @author dingpeihua
// * @date 2022/1/12 15:00
// * @version 1.0
// */
//fun StringBuilder.deleteEndChar(endChar: String): StringBuilder {
//    val index = lastIndexOf(endChar)
//    if (isNotEmpty() && index == length - endChar.length) {
//        delete(index, length)
//    }
//    return this
//}
//
//inline fun <C : CharSequence> C?.ifNullOrEmpty(defaultValue: () -> C): C =
//    if (isNullOrEmpty()) defaultValue() else this
//
//@SinceKotlin("1.3")
//inline fun <C : CharSequence> C?.ifNullOrBlank(defaultValue: () -> C): C =
//    if (isNullOrBlank()) defaultValue() else this
//
//
//@OptIn(ExperimentalContracts::class)
//fun CharSequence?.ifEmptyOrBlank(defaultValue: () -> CharSequence): CharSequence {
//    contract {
//        returns(false) implies (this@ifEmptyOrBlank != null)
//    }
//    return this ?: defaultValue()
//}