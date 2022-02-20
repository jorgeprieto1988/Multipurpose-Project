package edu.uoc.android.PEC3App

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 *Splash Activity
 * Calls mainactivity after showing UOC logo
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
    }
}
