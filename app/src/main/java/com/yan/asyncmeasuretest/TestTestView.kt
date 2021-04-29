package com.yan.asyncmeasuretest

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author Bevan (Contact me: https://github.com/genius158)
 * @since  2021/4/29
 */
class TestTestView(ctx: Context, attr: AttributeSet) : AppCompatTextView(ctx, attr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("TestTestView", "onMeasure onMeasure onMeasure  "+Thread.currentThread())
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.e("TestTestView", "onLayout onLayout onLayout  "+Thread.currentThread())
    }

    override fun requestLayout() {
        super.requestLayout()
    }

    override fun invalidateOutline() {
        super.invalidateOutline()
    }

    override fun onDraw(canvas: Canvas?) {
//        Log.e("onDraw","onDraw onDraw ")
        super.onDraw(canvas)
    }
}