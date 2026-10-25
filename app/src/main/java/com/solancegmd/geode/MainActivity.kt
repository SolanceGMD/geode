package com.solancegmd.geode

import android.app.Activity
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
            text = "Geode Wallpaper Guard"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }
        val info = TextView(this).apply {
            text = "Keep Backround.png as your home-screen wallpaper. When Android reports that another wallpaper was set, this app will restore your image. A persistent notification is required while protection is active."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 24)
        }
        val enable = Button(this).apply {
            text = "Enable wallpaper protection"
            setOnClickListener {
                getSharedPreferences(WallpaperGuardService.PREFS, MODE_PRIVATE)
                    .edit().putBoolean(WallpaperGuardService.KEY_ENABLED, true).apply()
                val intent = Intent(this@MainActivity, WallpaperGuardService::class.java)
                startForegroundService(intent)
            }
        }
        val disable = Button(this).apply {
            text = "Disable protection"
            setOnClickListener {
                getSharedPreferences(WallpaperGuardService.PREFS, MODE_PRIVATE)
                    .edit().putBoolean(WallpaperGuardService.KEY_ENABLED, false).apply()
                stopService(Intent(this@MainActivity, WallpaperGuardService::class.java))
            }
        }
        layout.addView(title)
        layout.addView(info)
        layout.addView(enable)
        layout.addView(disable)
        setContentView(layout)
    }
}
