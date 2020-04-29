package com.song.kotlinapp

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.RelativeLayout
/**
 * @author : songjun
 * @create : 2020/4/29
 */
class FloatView @JvmOverloads constructor(context :Context,attributeSet: AttributeSet,defStyleAttr: Int = 0) :
    RelativeLayout(context,attributeSet,defStyleAttr),View.OnTouchListener{

    private var parentHeight //悬浮的父布局高度
            = 0
    private var parentWidth = 0
    private var lastX = 0
    private var lastY = 0
    private var downX = 0
    private var downY = 0
    private var isDraging //是否正在拖拽
            = false

    init {

    }

    fun setView(floatView: View?, startX: Int, startY: Int) {
        addView(floatView)
        this.x = startX.toFloat()
        this.y = startY.toFloat()
        setOnTouchListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val view = getChildAt(0)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val rawX = event!!.rawX.toInt()
        val rawY = event.rawY.toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDraging){
                    isDraging = parentHeight > 0 && parentWidth > 0 //只有父布局存在你才可以拖动
                    val dx = rawX - lastX
                    val dy = rawY - lastY
                    var x = x + dx
                    var y = y + dy
                    //检测是否到达边缘 左上右下
                    x = if (x < 0) 0f else if (x > parentWidth - width) (parentWidth - width).toFloat() else x
                    y = if (y < 0) 0f else if (y > parentHeight - height) (parentHeight - height).toFloat() else y
                    setX(x)
                    setY(y)
                    lastX = rawX
                    lastY = rawY
                }
            }
            MotionEvent.ACTION_UP -> moveHide(rawX, rawY)
        }

        //如果不是拖拽，那么就不消费这个事件，以免影响点击事件的处理，拖拽事件要自己消费
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var isIntercept = false //不拦截表示未在拖动
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x.toInt()
                downY = ev.y.toInt()
                //                getParent().requestDisallowInterceptTouchEvent(true);//父布局不要拦截子布局的监听
                lastX = ev.rawX.toInt()
                lastY = ev.rawY.toInt()
                val parent = parent as ViewGroup
                if (parent != null) {
                    parentHeight = parent.height
                    parentWidth = parent.width
                }
                isIntercept = false
            }
            MotionEvent.ACTION_MOVE ->  //如果有效自己处理触摸事件
                isIntercept = Math.abs(ev.x - downX) > 5 && Math.abs(ev.y - downY) > 5
            MotionEvent.ACTION_UP -> {
            }
        }
        isDraging = isIntercept
        return isIntercept
    }

    private fun moveHide(rawX: Int, rawY: Int) {
        var oaX: ObjectAnimator? = null
        oaX = if (rawX >= parentWidth / 2) { //靠右吸附
            ObjectAnimator.ofFloat(this, "x", x,
                    parentWidth - width.toFloat())
        } else { //靠左吸附
            ObjectAnimator.ofFloat(this, "x", x, 0f)
        }
        oaX.interpolator = BounceInterpolator()
        oaX.duration = 500
        oaX.start()
        if (rawY <= ScreenUtil.dpToPx(112f,context)) { //这里的112是距底部的距离加上控件一半的高
            val oaY = ObjectAnimator.ofFloat(this, "y", y,
                    ScreenUtil.dpToPx(82f,context).toFloat())
            oaY.interpolator = BounceInterpolator()
            oaY.duration = 500
            oaY.start()
        }
    }

}