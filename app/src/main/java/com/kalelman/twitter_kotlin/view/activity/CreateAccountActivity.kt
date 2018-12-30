package com.kalelman.twitter_kotlin.view.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.SIGN_UP
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SignUpCallback
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.layout_custom_alert_builder_signup.*
import kotlinx.android.synthetic.main.layout_custom_alert_builder_signup.view.*

class CreateAccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
    }

    fun onCreateAccountClicked(view: View) {
        val tietUserName = tiet_user_name.text.toString()
        val tietEamil = tiet_email.text.toString()
        val tietPassword = tiet_password_ca.text.toString()
        val tietConfirmPassword = tiet_password_confirm.text.toString()

        Tools.hideKeyboard(this)

        if (TextUtils.isEmpty(tietUserName) && TextUtils.isEmpty(tietEamil) && TextUtils.isEmpty(tietPassword) && TextUtils.isEmpty(tietConfirmPassword)) {
            til_username.error = getString(R.string.text_required_field)
            til_email.error = getString(R.string.text_required_field)
            til_password_ca.error = getString(R.string.text_required_field)
            til_password_confirm.error = getString(R.string.text_required_field)
        } else if (TextUtils.isEmpty(tietUserName)) {
            til_username.error = getString(R.string.text_required_field)
            til_email.error = null
            til_password_ca.error = null
            til_password_confirm.error = null
        } else if (TextUtils.isEmpty(tietEamil)) {
            til_username.error = null
            til_email.error = getString(R.string.text_required_field)
            til_password_ca.error = null
            til_password_confirm.error = null
        } else if (!Tools.isValidEmail(tietEamil)) {
            til_username.error = null
            til_email.error = getString(R.string.text_invalid_email)
            til_password_ca.error = null
            til_password_confirm.error = null
        } else if (TextUtils.isEmpty(tietPassword)) {
            til_username.error = null
            til_email.error = null
            til_password_ca.error = getString(R.string.text_required_field)
            til_password_confirm.error = null
        } else if(TextUtils.isEmpty(tietConfirmPassword)) {
            til_username.error = null
            til_email.error = null
            til_password_ca.error = null
            til_password_confirm.error = getString(R.string.text_required_field)
        } else if (tietPassword != tietConfirmPassword) {
            til_username.error = null
            til_email.error = null
            til_password_ca.error = getString(R.string.text_password_not_match)
            til_password_confirm.error = getString(R.string.text_password_not_match)
        } else {
            til_username.error = null
            til_email.error = null
            til_password_ca.error = null
            til_password_confirm.error = null
            Toast.makeText(this, "Pasaste", Toast.LENGTH_LONG).show()
            //btnSignUp.isEnabled = false
            //signUp()
        }
    }

    private fun signUp() {
        val tietUserName = tiet_user_name.text.toString()
        val tietPassword = tiet_password_ca.text.toString()
        val tietEamil = tiet_email.text.toString()

        val newUser = ParseUser()
        newUser.username = tietUserName
        newUser.setPassword(tietPassword)
        newUser.email = tietEamil
        newUser.signUpInBackground { e ->
            if (e == null) {
                Log.i(SIGN_UP, SUCCES_MESSAGE)
                Tools.hideKeyboard(this)
                redirectLogin()
            } else {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectLogin() {
        // Inflates the dialog with custom view
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.layout_custom_alert_builder_signup, null)
        val btnAlert = btn_alert_succes
        dialog.setView(dialogView)
        btnAlert.setOnClickListener(View.OnClickListener { dialog.setOnDismissListener { this } })
        dialog.show()



    }

}
