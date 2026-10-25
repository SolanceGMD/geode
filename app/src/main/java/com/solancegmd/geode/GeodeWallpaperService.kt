package com.solancegmd.geode

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperManager
import kotlin.math.max

class GeodeWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = GeodeEngine()

    inner class GeodeEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var bitmap: Bitmap? = null
        private var xOffset = 0.5f
        private var visible = false
        private val paint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG)
        private val drawRunner = Runnable { drawFrame() }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            bitmap = try {
                assets.open("Backround.png").use { BitmapFactory.decodeStream(it) }
            } catch (_: Exception) { null }
            setTouchEventsEnabled(false)
        }

        override fun onVisibilityChanged(isVisible: Boolean) {
            visible = isVisible
            if (isVisible) drawFrame() else handler.removeCallbacks(drawRunner)
        }

        override fun onOffsetsChanged(xOffset: Float, yOffset: Float, xOffsetStep: Float, yOffsetStep: Float, xPixelOffset: Int, yPixelOffset: Int) {
            this.xOffset = xOffset
            drawFrame()
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            drawFrame()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            visible = false
            handler.removeCallbacks(drawRunner)
            super.onSurfaceDestroyed(holder)
        }

        override fun onDestroy() {
            visible = false
            handler.removeCallbacks(drawRunner)
            bitmap?.recycle()
            bitmap = null
            super.onDestroy()
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    val w = canvas.width.toFloat()
                    val h = canvas.height.toFloat()
                    canvas.drawColor(Color.rgb(18, 20, 28))
                    val image = bitmap
                    if (image != null && image.width > 0 && image.height > 0) {
                        // Fill the screen height and allow horizontal panning across launcher pages.
                        val scale = max(w / image.width, h / image.height)
                        val drawW = image.width * scale
                        val drawH = image.height * scale
                        val extraX = max(0f, drawW - w)
                        val left = -extraX * xOffset.coerceIn(0f, 1f)
                        val top = (h - drawH) / 2f
                        canvas.drawBitmap(image, null, Rect(left.toInt(), top.toInt(), (left + drawW).toInt(), (top + drawH).toInt()), paint)
                    } else {
                        paint.color = Color.WHITE
                        paint.textSize = 42f
                        canvas.drawText("Add Backround.png to app/src/main/assets/", 24f, h / 2f, paint)
                        paint.color = Color.WHITE
                    }
                }
            } catch (_: Exception) {
            } finally {
                if (canvas != null) try { holder.unlockCanvasAndPost(canvas) } catch (_: Exception) {}
            }
            handler.removeCallbacks(drawRunner)
            if (visible) handler.postDelayed(drawRunner, 1000L)
        }
    }
}
