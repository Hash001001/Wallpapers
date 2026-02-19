package com.securetech.wallpapers

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashDuration = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        startAnimations()

        lifecycleScope.launch {
            delay(splashDuration)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    private fun startAnimations() {
        val iconContainer = findViewById<View>(R.id.iconContainer)
        val appTitle = findViewById<View>(R.id.appTitle)
        val appTagline = findViewById<View>(R.id.appTagline)
        val glowEffect = findViewById<View>(R.id.glowEffect)
        val loadingContainer = findViewById<View>(R.id.loadingContainer)

        // Icon: scale up + fade in with overshoot
        val iconScaleX = ObjectAnimator.ofFloat(iconContainer, View.SCALE_X, 0.3f, 1f).apply {
            duration = 800
            interpolator = OvershootInterpolator(1.5f)
        }
        val iconScaleY = ObjectAnimator.ofFloat(iconContainer, View.SCALE_Y, 0.3f, 1f).apply {
            duration = 800
            interpolator = OvershootInterpolator(1.5f)
        }
        val iconAlpha = ObjectAnimator.ofFloat(iconContainer, View.ALPHA, 0f, 1f).apply {
            duration = 600
        }

        // Glow: fade in slowly
        val glowAlpha = ObjectAnimator.ofFloat(glowEffect, View.ALPHA, 0f, 0.7f).apply {
            duration = 1200
            startDelay = 300
        }
        val glowScaleX = ObjectAnimator.ofFloat(glowEffect, View.SCALE_X, 0.5f, 1.2f).apply {
            duration = 1500
            startDelay = 300
        }
        val glowScaleY = ObjectAnimator.ofFloat(glowEffect, View.SCALE_Y, 0.5f, 1.2f).apply {
            duration = 1500
            startDelay = 300
        }

        // Title: slide up + fade in
        val titleTranslation = ObjectAnimator.ofFloat(appTitle, View.TRANSLATION_Y, 40f, 0f).apply {
            duration = 700
            startDelay = 500
            interpolator = DecelerateInterpolator()
        }
        val titleAlpha = ObjectAnimator.ofFloat(appTitle, View.ALPHA, 0f, 1f).apply {
            duration = 700
            startDelay = 500
        }

        // Tagline: slide up + fade in
        val taglineTranslation = ObjectAnimator.ofFloat(appTagline, View.TRANSLATION_Y, 30f, 0f).apply {
            duration = 600
            startDelay = 800
            interpolator = DecelerateInterpolator()
        }
        val taglineAlpha = ObjectAnimator.ofFloat(appTagline, View.ALPHA, 0f, 1f).apply {
            duration = 600
            startDelay = 800
        }

        // Loading indicator: fade in
        val loadingAlpha = ObjectAnimator.ofFloat(loadingContainer, View.ALPHA, 0f, 1f).apply {
            duration = 500
            startDelay = 1200
        }

        AnimatorSet().apply {
            playTogether(
                iconScaleX, iconScaleY, iconAlpha,
                glowAlpha, glowScaleX, glowScaleY,
                titleTranslation, titleAlpha,
                taglineTranslation, taglineAlpha,
                loadingAlpha
            )
            start()
        }
    }
}
