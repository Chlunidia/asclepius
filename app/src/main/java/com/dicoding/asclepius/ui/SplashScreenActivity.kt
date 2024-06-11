package com.dicoding.asclepius.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        hideActionBar()

        val delayHandler = Handler(Looper.getMainLooper())
        delayHandler.postDelayed({
            launchMainActivity()
        }, SPLASH_DURATION)
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun launchMainActivity() {
        val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    companion object {
        private const val SPLASH_DURATION: Long = 2500
    }
}