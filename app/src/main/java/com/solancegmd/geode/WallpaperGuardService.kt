package com.solancegmd.geode

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log

class WallpaperGuardService : Service() {
    companion object {
        const val PREFS = "wallpaper_guard"
        const val KEY_ENABLED = "enabled"
        private const val KEY_WALLPAPER_ID = "last_applied_wallpaper_id"
        private const val CHANNEL_ID = "wallpaper_guard"
        private const val NOTIFICATION_ID = 7314
        private const val TAG = "WallpaperGuard"
    }

    private var receiverRegistered = false
    private var applyingWallpaper = false

    private val wallpaperReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != Intent.ACTION_WALLPAPER_CHANGED || applyingWallpaper) return
            val manager = WallpaperManager.getInstance(this@WallpaperGuardService)
            val currentId = manager.getWallpaperId(WallpaperManager.FLAG_SYSTEM)
            val savedId = getSharedPreferences(PREFS, MODE_PRIVATE).getInt(KEY_WALLPAPER_ID, -1)
            if (currentId != savedId) {
                restoreOurWallpaper()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification())
        try {
            registerReceiver(wallpaperReceiver, IntentFilter(Intent.ACTION_WALLPAPER_CHANGED))
            receiverRegistered = true
        } catch (e: Exception) {
            Log.e(TAG, "Unable to register wallpaper-change listener", e)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val enabled = getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(KEY_ENABLED, false)
        if (!enabled) {
            stopSelf()
            return START_NOT_STICKY
        }
        restoreOurWallpaper()
        return START_STICKY
    }

    private fun restoreOurWallpaper() {
        if (applyingWallpaper) return
        applyingWallpaper = true
        try {
            val manager = WallpaperManager.getInstance(this)
            assets.open("Backround.png").use { input ->
                val bitmap = BitmapFactory.decodeStream(input)
                    ?: throw IllegalStateException("Backround.png is not a valid image")
                try {
                    manager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                } finally {
                    bitmap.recycle()
                }
            }
            val id = manager.getWallpaperId(WallpaperManager.FLAG_SYSTEM)
            getSharedPreferences(PREFS, MODE_PRIVATE).edit().putInt(KEY_WALLPAPER_ID, id).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Could not apply app/src/main/assets/Backround.png", e)
            stopForeground(true)
            stopSelf()
        } finally {
            applyingWallpaper = false
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Wallpaper protection", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        return builder.setContentTitle("Wallpaper protection is on")
            .setContentText("Restoring Backround.png when a wallpaper change is detected")
            .setSmallIcon(android.R.drawable.ic_menu_gallery)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        if (receiverRegistered) {
            try { unregisterReceiver(wallpaperReceiver) } catch (_: Exception) {}
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
