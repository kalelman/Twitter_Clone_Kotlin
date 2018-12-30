package com.kalelman.twitter_kotlin.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.LOGIN_TAG
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import com.parse.ParseInstallation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Save the current Installation of the App in Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground()
    }

    fun onCreatedAccountClicked(view: View) {
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    fun onLoginClicked(view: View) {
        /*var tietUsername = findViewById<TextInputEditText>(R.id.tiet_username)
        var tietPassword = findViewById<TextInputEditText>(R.id.tiet_password)
        var tilUserName = findViewById<TextInputLayout>(R.id.til_user)
        var tilPassword = findViewById<TextInputLayout>(R.id.til_password)*/
        val tietUsername = tiet_username.text.toString()
        val tietPassword = tiet_password.text.toString()

        Tools.hideKeyboard(this)
        if (TextUtils.isEmpty(tietUsername) && TextUtils.isEmpty(tietPassword)) {
            til_user.error = getString(R.string.text_required_field)
            til_password.error = getString(R.string.text_required_field)
        } else if (TextUtils.isEmpty(tietUsername)) {
            til_user.error = getString(R.string.text_required_field)
            til_password.error = null
            //tilUserName.setError(resources.getText(R.string.text_required_field))
            //tilPassword.setError(null)
        } else if (TextUtils.isEmpty(tietPassword)) {
            til_password.error = getString(R.string.text_required_field)
            til_user.error = null
            //tilPassword.setError(resources.getText(R.string.text_required_field))
            //tilUserName.setError(null)
        } else {
            til_user.error = null
            til_password.error = null
            //showProgress(true)
            signIn(tietUsername, tietPassword)

            //tilUserName.setError(null)
            //tilPassword.setError(null)
            //btnLogin.setEnabled(false);
    }


    }

    private fun showProgress(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    private fun signIn(user: String, password: String) {
        ParseUser.logInInBackground(user, password) { user, e ->
            if (e == null) {
                Log.i(LOGIN_TAG, SUCCES_MESSAGE)
                //showProgress();
                startActivity(Intent(this, ContainerMainActivity::class.java))
            } else {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
