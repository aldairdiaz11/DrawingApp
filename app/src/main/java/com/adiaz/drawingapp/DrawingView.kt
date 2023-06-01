package com.adiaz.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var absDrawPath : CustomPath? = null
    private var absCanvasBitmap : Bitmap? = null
    private var absDrawPaint : Paint? = null
    private var absCanvasPaint : Paint? = null
    private var absBrushSize : Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas : Canvas? = null

    init {
        setUpDrawing()
    }

    private fun setUpDrawing() {
        absDrawPaint = Paint()
        absDrawPath = CustomPath(color, absBrushSize)
        absDrawPaint!!.color = color
        absDrawPaint!!.style = Paint.Style.STROKE
        absDrawPaint!!.strokeJoin = Paint.Join.ROUND
        absDrawPaint!!.strokeCap = Paint.Cap.ROUND
        absCanvasPaint = Paint(Paint.DITHER_FLAG)
        absBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        absCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(absCanvasBitmap!!)
    }

    // changed Canvas? revert if fails
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(absCanvasBitmap!!, 0f, 0f, absDrawPaint)

        if (!absDrawPath!!.isEmpty) {
            absDrawPaint!!.strokeWidth = absDrawPath!!.brushThickness
            absDrawPaint!!.color = absDrawPath!!.color
            canvas.drawPath(absDrawPath!!, absDrawPaint!!)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                absDrawPath!!.color = color
                absDrawPath!!.brushThickness = absBrushSize

                absDrawPath!!.reset()
                absDrawPath!!.moveTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_MOVE -> {
                absDrawPath!!.lineTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_UP -> {
                absDrawPath = CustomPath(color, absBrushSize)
            }

            else -> return false
        }

        invalidate()

        return true
    }

    internal inner class CustomPath(var color: Int, var brushThickness: Float)
        : android.graphics.Path() {

    }

}