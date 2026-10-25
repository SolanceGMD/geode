package com.solancegmd.geode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val enabled = context.getSharedPreferences(WallpaperGuardService.PREFS, Context.MODE_PRIVATE)
            .getBoolean(WallpaperGuardService.KEY_ENABLED, false)
        if (enabled) {
            val service = Intent(context, WallpaperGuardService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(service)
            else context.startService(service)
        }
    }
}
