package com.kurilov.worldscillscomponent.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import android.graphics.*
import androidx.core.view.ViewCompat

class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr : Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr){

    var radius = 30f
        set(value) {
            field = value
            generateMask(width, height)
            invalidate()
        }


    private var path : Path? = null
    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas?) {

        if (canvas != null) {
            if (canvas.isOpaque) {
                canvas.saveLayerAlpha(RectF(0f,0f, canvas.width.toFloat(), canvas.height.toFloat()), 255)
            }
        }

        super.onDraw(canvas)

        path?.let {
            canvas?.drawPath(it, paint)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (w != oldw || h != oldh)
            generateMask(w, h)
    }

    private fun generateMask(w : Int, h : Int) {
        path = Path()
        path!!.addRoundRect(RectF(0f,0f, w.toFloat(), h.toFloat()), radius, radius, Path.Direction.CW)
        path!!.fillType = Path.FillType.INVERSE_WINDING
    }

}

