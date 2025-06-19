package com.mera.babycare

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class BaseActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupEdgeToEdge()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setupEdgeToEdge() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = getColor(android.R.color.transparent)
        window.isNavigationBarContrastEnforced = false

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightNavigationBars = false

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun getLoggedUserId(): String? {
        val prefs = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return prefs.getString("user_id", null)
    }

    fun isUserLoggedIn(): Boolean {
        return getLoggedUserId() != null
    }
}
