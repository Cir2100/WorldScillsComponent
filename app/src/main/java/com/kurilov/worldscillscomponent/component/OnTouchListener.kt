package com.kurilov.worldscillscomponent.component

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import java.lang.Exception
import kotlin.math.abs

open class OnSwipeListener(context: Context) : OnTouchListener {

    private val gestureDetector : GestureDetector

    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeDown() {}

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private val SWIPE_THRESHOLD : Int = 100
        private val SWIPE_VELOCITY_THRESHOLD : Int = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            var result = false
            try {
                val diffX : Float = e2.x - e1.x
                val diffY : Float = e2.y - e1.y
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0)
                            onSwipeRight()
                        else
                            onSwipeLeft()
                        result = true
                    }
                }else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0)
                        onSwipeTop()
                    else
                        onSwipeDown()
                    result = true
                }
            }catch (e : Exception) {
                e.printStackTrace()
            }
            return result
        }
    }
}