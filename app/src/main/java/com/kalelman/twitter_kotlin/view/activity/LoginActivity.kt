package com.kalelman.twitter_kotlin.view.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.LOGIN_TAG
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import com.parse.ParseInstallation
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @autor Erick Rojas Perez</br><br>erick_rojas_perez@hotmail.com<br>
 * @date January/03/2019</br>
 * @description Activity for manage the Login process in Twitter Clone</br>
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Save the current Installation of the App in Back4App
        ParseInstallation.getCurrentInstallation().saveInBackground()
    }

    /**
     * Go to CreateAccountActivity
     */
    fun onCreatedAccountClicked(view: View) {
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    /**
     * Method that validate and execute the Login process and notify to Backend
     */
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

    /**
     * Go to RecoverPasswordActivity
     */
    fun onRecoverPasswordClicked(view: View) {
        startActivity(Intent(this, RecoverPasswordActivity::class.java))
    }

    /**
     * Show ProgressDialog in Login Process
     */
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

    /**
     * Login process and notify to Backend
     * go to ContainerMainActivity
     */
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
