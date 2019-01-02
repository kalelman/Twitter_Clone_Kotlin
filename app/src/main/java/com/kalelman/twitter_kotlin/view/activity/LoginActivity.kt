package com.kalelman.twitter_kotlin.view.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.LOGIN_TAG
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import com.parse.ParseInstallation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.til_user)
    lateinit var tilUser: TextInputLayout
    @BindView(R.id.til_password)
    lateinit var tilPassword: TextInputLayout
    @BindView(R.id.tiet_username)
    lateinit var tietUsername: TextInputEditText
    @BindView(R.id.tiet_password)
    lateinit var tietPassword: TextInputEditText
    @BindView(R.id.btn_login)
    lateinit var btnLogIn: Button

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
        val tietUsername = tiet_username.text.toString()
        val tietPassword = tiet_password.text.toString()
        Tools.hideKeyboard(this)
        if (TextUtils.isEmpty(tietUsername) && TextUtils.isEmpty(tietPassword)) {
            til_user.error = getString(R.string.text_required_field)
            til_password.error = getString(R.string.text_required_field)
        } else if (TextUtils.isEmpty(tietUsername)) {
            til_user.error = getString(R.string.text_required_field)
            til_password.error = null
        } else if (TextUtils.isEmpty(tietPassword)) {
            til_user.error = null
            til_password.error = getString(R.string.text_required_field)
        } else {
            til_user.error = null
            til_password.error = null
            showProgress()
            signIn(tietUsername, tietPassword)
            //btn_login.isEnabled = false

        }
    }

    fun onRecoverPasswordClicked(view: View) {
        startActivity(Intent(this, RecoverPasswordActivity::class.java))
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.layout_progress_dialog, null)
            val message = dialogView.findViewById<TextView>(R.id.message)
            message.text = "Loading..."
            builder.setView(dialogView)
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
            Handler().postDelayed({dialog.dismiss()}, 1000)
        }
    }

    private fun signIn(user: String, password: String) {
        ParseUser.logInInBackground(user, password) { user, e ->
            if (e == null) {
                Log.i(LOGIN_TAG, SUCCES_MESSAGE)
                //showProgress(false)
                startActivity(Intent(this, ContainerMainActivity::class.java))
            } else {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
