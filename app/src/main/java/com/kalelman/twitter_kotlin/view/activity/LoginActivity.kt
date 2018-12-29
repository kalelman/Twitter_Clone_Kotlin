package com.kalelman.twitter_kotlin.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kalelman.twitter_kotlin.R
import com.parse.ParseInstallation

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Save the current Installation of the App in Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground()
    }
}
