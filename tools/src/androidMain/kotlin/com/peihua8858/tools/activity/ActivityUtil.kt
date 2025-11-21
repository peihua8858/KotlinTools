@file:JvmName("ActivityUtil")
@file:JvmMultifileClass

package com.peihua8858.tools.activity

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import com.peihua8858.tools.ContextInitializer
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.peihua8858.tools.utils.resolveAttribute

@ColorInt
fun Activity?.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = this ?: ContextInitializer.context
    return ContextCompat.getColor(context, colorRes)
}

fun Activity?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context: Context = this ?: ContextInitializer.context
    return ContextCompat.getDrawable(context, drawableRes)
}

fun Activity?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = this ?: ContextInitializer.context
    return context.getDimens(resId)
}

fun Activity?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = this ?: ContextInitializer.context
    return context.getString(resourceId, *formatArgs)
}

fun Activity?.getString(@StringRes resourceId: Int): String {
    val context: Context = this ?: ContextInitializer.context
    return context.getString(resourceId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun Activity?.getResourceId(attrId: Int): Int {
    val context: Context = this ?: ContextInitializer.context
    return context.getResourceId(attrId)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context    当前上下文
 * @param resId      资源ID
 * @param defaultRes 默认主题样式
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun Activity?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = this ?: ContextInitializer.context
    return context.resolveAttribute(resId, defaultRes)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context 当前上下文
 * @param resId   资源ID
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun Activity?.resolveAttribute(resId: Int): Int {
    val context: Context = this ?: ContextInitializer.context
    return context.resolveAttribute(resId)
}

fun Context?.findActivity(): Activity? {
    return findActivity((this))
}

fun Context?.getActivity(): Activity? {
    return getActivity(this)
}

fun Any?.getActivity(context: Context?): Activity? {
    var cxt = context
    while (cxt is ContextWrapper) {
        if (cxt is Activity) {
            return cxt
        }
        cxt = cxt.baseContext
    }
    return null
}

/**
 * 查找当前上下文activity
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:38
 * @version 1.0
 */
fun Any?.findActivity(context: Context?): Activity? {
    return getActivity(context)
}

/**
 * 判断activity是否销毁
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Context?.isDestroy(): Boolean {
    return isDestroy(findActivity(this))
}

fun Context?.isFinish(): Boolean {
    return isFinish(this.findActivity())
}

/**
 * 判断activity是否关闭
 *
 * @param context
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(context: Context): Boolean {
    return isFinish(findActivity(context))
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Activity?.isDestroy(): Boolean {
    return this == null || this.isDestroyed
}

/**
 * 判断activity是否销毁
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isDestroy(activity: Activity?): Boolean {
    return activity == null || activity.isDestroyed
}

/**
 * 判断activity是否关闭
 *
 * @param activity
 * @author dingpeihua
 * @date 2019/11/19 15:39
 * @version 1.0
 */
fun Any?.isFinish(activity: Activity?): Boolean {
    return activity == null || activity.isFinishing
}

/**
 * 创建协程上下文并处理回调异常到[onError]
 * @author dingpeihua
 * @date 2021/2/17 10:58
 * @version 1.0
 */
fun ComponentActivity.createHandler(onError: (CoroutineContext, Throwable) -> Unit): CoroutineContext {
    return CoroutineExceptionHandler { context, e -> onError(context, e) }
}

/**
 * 共享元素缩放动画
 *
 * @param intent
 * @param shareView
 */
fun Context?.startActivityByShareElement(intent: Intent, shareView: View? = null) {
    if (this != null) {
        if (shareView != null) {
            val compat =
                ActivityOptionsCompat.makeScaleUpAnimation(shareView, shareView.width / 2, shareView.height / 2, 0, 0)
            ActivityCompat.startActivity(this, intent, compat.toBundle())
        } else {
            this.startActivity(intent)
        }
    }
}
