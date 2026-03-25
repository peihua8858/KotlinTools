package com.peihua8858.tools.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.EditText
import android.widget.PopupWindow
import androidx.core.app.ComponentActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.peihua8858.tools.activity.statusBarHeight

/**
 *
 * 监听键盘显示和隐藏
 * @date 2024/5/13 14:13
 **/
class SortKeyBoardStateHelper(
    private val activity: ComponentActivity,
    private val isImmersiveStatusBar: Boolean = false,
) : OnGlobalLayoutListener, DefaultLifecycleObserver, ViewTreeObserver.OnGlobalFocusChangeListener {

    private var wasOpened = false
    private var listener: ((isShow: Boolean) -> Unit)? = null
    private var mPopupWindow: PopupWindow? = null
    private val rootView = View(activity)

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        mPopupWindow = PopupWindow(activity)
        try {
            rootView.setLayoutParams(ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT))
            mPopupWindow?.setContentView(rootView)
            mPopupWindow?.softInputMode =
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
            mPopupWindow?.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
            mPopupWindow?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            mPopupWindow?.isTouchable = false
        } catch (e: Exception) {
            eLog { "observerKeyboard failed: ${e.message}" }
        }
        activity.checkSortInputMode()
        activity.viewTreeObserver.addOnGlobalFocusChangeListener(this)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            val windowToken = activity.windowToken
            if (windowToken != null && mPopupWindow?.isShowing == false) {
                mPopupWindow?.showAtLocation(
                    activity.contentRoot,
                    Gravity.NO_GRAVITY,
                    0,
                    0
                )
            }
        }
    }

    override fun onGlobalFocusChanged(oldFocus: View?, newFocus: View?) {
        dLog { "onGlobalFocusChanged: oldFocus=$oldFocus, newFocus=$newFocus" }
        onWindowFocusChanged(newFocus?.context == activity)
    }

    fun setOnKeyBoardStateListener(listener: (isShow: Boolean) -> Unit) {
        this.listener = listener
    }

    override fun onGlobalLayout() {
        var displayHeight = activity.window.decorView.height
        if (isImmersiveStatusBar) {
            val statusBarHeight = activity.statusBarHeight
            displayHeight -= statusBarHeight
        }
        //监听键盘弹起
        val isOpen = displayHeight > rootView.height
        if (isOpen == wasOpened) {
            //keyboard state has not changed
            return
        }
        wasOpened = isOpen
        listener?.invoke(isOpen)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        activity.viewTreeObserver.removeOnGlobalFocusChangeListener(this)
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        mPopupWindow?.dismiss()
        activity.lifecycle.removeObserver(this)
    }

    fun hideKeyboard(ev: MotionEvent) {
        if (activity.isNeedHideKeyboard(ev)) {
            activity.hideSoftKeyboard()
        }
    }
}


val Activity.viewTreeObserver: ViewTreeObserver
    get() = activityRoot.viewTreeObserver
val Activity.windowToken: IBinder?
    get() = window.decorView.windowToken

private const val KEYBOARD_MIN_HEIGHT_RATIO = 0.15
val Activity.isKeyboardVisible: Boolean
    get() {
        checkSortInputMode()
        val r = Rect()
        val activityRoot = activityRoot
        activityRoot.getWindowVisibleDisplayFrame(r)
        val location = IntArray(2)
        contentRoot.getLocationOnScreen(location)
        val screenHeight = activityRoot.rootView.height
        val heightDiff = screenHeight - r.height() - location[1]
        return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
    }

// 判定是否需要隐藏
fun Activity.isNeedHideKeyboard(ev: MotionEvent): Boolean {
    return currentFocus.isNeedHideKeyboard(ev)
}

fun View?.isNeedHideKeyboard(ev: MotionEvent): Boolean {
    if (this is EditText) {
        val l = intArrayOf(0, 0)
        this.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom: Int = top + this.height
        val right: Int = left + this.width
        return !(ev.x > left && ev.x < right && ev.y > top && ev.y < bottom)
    }
    return false
}

val Activity.activityRoot: View
    get() = contentRoot.rootView
val Activity.contentRoot: ViewGroup
    get() = findViewById(android.R.id.content)

fun Activity.checkSortInputMode() {
    val softInputAdjust =
        window.attributes.softInputMode and (WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
                or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    // fix for #37 and #38.
    // The window will not be resized in case of SOFT_INPUT_ADJUST_NOTHING
    val isNotAdjustNothing =
        softInputAdjust and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
    require(isNotAdjustNothing) { "Parameter:activity window SoftInputMethod is SOFT_INPUT_ADJUST_NOTHING. In this case window will not be resized" }
}

fun Activity?.showSoftKeyboard() {
    if (this == null) {
        return
    }
    this.currentFocus?.showSoftKeyboard()
}

/**
 * 显示软键盘
 *
 * @param [this] 控件
 */
fun View?.showSoftKeyboard() {
    if (this == null) {
        return
    }
    val context = this.context
    if (context != null) {
        try {
            val imm = context.inputMethodManager
            imm?.showSoftInput(this, 0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

fun Activity?.hideSoftKeyboard() {
    if (this == null) {
        return
    }
    this.currentFocus?.hideSoftKeyboard()
}

/**
 * 隐藏软键盘
 *
 * @param [this]    控件
 */
fun View?.hideSoftKeyboard() {
    if (this == null) {
        return
    }
    val context = this.context
    if (context != null) {
        try {
            val imm = context.inputMethodManager
            val token = this.windowToken
            if (token != null) {
                imm?.hideSoftInputFromWindow(token, 0)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}