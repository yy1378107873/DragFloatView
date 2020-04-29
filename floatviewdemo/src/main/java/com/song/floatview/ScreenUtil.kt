package com.song.kotlinapp

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue

/**
 * @author : songjun
 * @create : 2020/4/29
 */
object ScreenUtil {
    val widthPixels: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    val heightPixels: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    fun dpToPx(dipValue: Float, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics).toInt()
    }

    fun spToPx(spValue: Float, context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, metrics).toInt()
    }

    fun px2dip(context: Context, px: Float): Int { //获取手机密度
        val density = context.resources.displayMetrics.density
        //计算公式为dip = px * 手机密度
        return (px * density).toInt()
    }

    /**
     * 获取状态栏的高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = context.resources.getDimensionPixelSize(R.dimen.statusbar_default_height)
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun zW(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun zX(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun O(f: Float): Float {
        return f * Resources.getSystem().displayMetrics.density
    }

    fun P(f: Float): Float {
        return f * Resources.getSystem().displayMetrics.scaledDensity
    }

    /**
     * 获取 APP 显示高度
     * @param activity 当前活动状态的 Activity
     * @return AppHeight(include ActionBar) = Screen Height - StatusHeight
     */
    fun getAppHeight(activity: Activity?): Int {
        val rect = Rect()
        activity?.window?.decorView?.getWindowVisibleDisplayFrame(rect)
        return rect.height()
    }

    /**
     * 获取软键盘显示高度
     * @param activity 当前活动状态的 Activity
     * @return 软键盘高度 = 分辨率高 - 状态栏高 - 应用可视高,第一次获取,该值为787
     */
    fun getKeyBoardHeight(activity: Activity): Int {
        return heightPixels - getStatusBarHeight(activity) - getAppHeight(activity)
    }

    fun isSoftShowing(activity: Activity): Boolean { //获取当前屏幕内容的高度
        val screenHeight = activity.window.decorView.height
        //获取View可见区域的bottom
        val rect = Rect()
        //DecorView即为activity的顶级view
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
//选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom
    }
}