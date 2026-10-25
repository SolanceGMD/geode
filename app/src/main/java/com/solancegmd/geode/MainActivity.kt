package com.solancegmd.geode

import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
            setBackgroundColor(Color.rgb(20, 24, 34))
        }
        val title = TextView(this).apply {
            text = "Geode Wallpaper"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Choose the live wallpaper to use Backround.png. Swipe between home-screen pages to pan the image."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 24)
        }
        val button = Button(this).apply {
            text = "Set live wallpaper"
            setOnClickListener {
                val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                    putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@MainActivity, GeodeWallpaperService::class.java))
                }
                startActivity(intent)
            }
        }
        layout.addView(title)
        layout.addView(info)
        layout.addView(button)
        setContentView(layout)
    }
}
