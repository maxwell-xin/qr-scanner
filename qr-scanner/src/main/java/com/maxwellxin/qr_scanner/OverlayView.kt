package com.maxwellxin.qr_scanner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils

class OverlayView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint: Paint = Paint()
    private var recPaint: Paint = Paint()

    private var bitmap: Bitmap? = null
    private var layer: Canvas? = null

    init {
        //configure background color
        val backgroundAlpha = 0.8
        paint.color = ColorUtils.setAlphaComponent(resources.getColor(R.color.black, null), (255 * backgroundAlpha).toInt())

        //configure rectangle color & mode
        recPaint.color = resources.getColor(android.R.color.transparent, null)
        recPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //create bitmap and layer
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            layer = Canvas(bitmap!!)
        }

        //draw background
        layer?.drawRect(0.0f, 0.0f, width.toFloat(), height.toFloat(), paint)

        //draw rectangle
        var rectangle: Rect = getRectangle()
        layer?.drawRect(
            rectangle.left.toFloat(),
            rectangle.top.toFloat(),
            rectangle.right.toFloat(),
            rectangle.bottom.toFloat(),
            recPaint
        )

        //draw bitmap
        canvas.drawBitmap(bitmap!!, 0.0f, 0.0f, paint);
    }

    fun getRectangle(): Rect {
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int

        if (width > height) {
            top = (height * 0.3).toInt()
            bottom = (height * 0.6).toInt()
            left = (width * 0.5).toInt() - ((bottom - top) / 2)
            right = (width * 0.5).toInt() + ((bottom - top) / 2)
        } else {
            top = (height * 0.3).toInt()
            bottom = (height * 0.6).toInt()
            left = (width * 0.5).toInt() - ((bottom - top) / 2)
            right = (width * 0.5).toInt() + ((bottom - top) / 2)
        }

        return Rect(left, top, right, bottom)
    }
}