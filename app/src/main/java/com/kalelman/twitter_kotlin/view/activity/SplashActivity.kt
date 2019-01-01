package com.kalelman.twitter_kotlin.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kalelman.twitter_kotlin.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
