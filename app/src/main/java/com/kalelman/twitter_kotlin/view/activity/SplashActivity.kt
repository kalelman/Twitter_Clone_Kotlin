package com.kalelman.twitter_kotlin.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kalelman.twitter_kotlin.R

/**
 * @autor Erick Rojas Perez</br><br>erick_rojas_perez@hotmail.com</br>
 * @date January/03/2019</br>
 * @description Activity for showing a Splash Screen for the App and go to LoginActivity</br>
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
