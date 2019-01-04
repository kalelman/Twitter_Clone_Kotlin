package com.kalelman.twitter_kotlin.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.SIGN_UP
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import kotlinx.android.synthetic.main.activity_create_account.*
import com.parse.ParseUser
import kotlinx.android.synthetic.main.layout_custom_alert_builder_signup.view.*

/**
 * @author Erick Rojas Perez</br><br>erick_rojas_perez@hotmail.com</br>
 * @date January/03/2019
 * @description Activity for Create Account for User
 */
class CreateAccountActivity : ToolBar() {

    private val user = ParseUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTextTranslate()
        setTitleActionBar("")
    }

    /**
     * get the Layout resource and load in the container
     */
    override fun getLayoutResource(): Int {
        return R.layout.activity_create_account

    }

    /**
     * SetText for the custom view
     */
    private fun setTextTranslate() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_sign_up)
    }

    /**
     * Method that validate the input information for the user and
     * create a new account and save the data in Backend
     */
    fun onCreateAccountClicked(view: View) {
        val tietUserName = tiet_user_name.text.toString()
        val tietEamil = tiet_email.text.toString()
        val tietPassword = tiet_password_ca.text.toString()
        val tietConfirmPassword = tiet_password_confirm.text.toString()

        Tools.hideKeyboard(this)

        when {
            TextUtils.isEmpty(tietUserName) && TextUtils.isEmpty(tietEamil) && TextUtils.isEmpty(tietPassword) && TextUtils.isEmpty(tietConfirmPassword) -> {
                til_username.error = getString(R.string.text_required_field)
                til_email.error = getString(R.string.text_required_field)
                til_password_ca.error = getString(R.string.text_required_field)
                til_password_confirm.error = getString(R.string.text_required_field)
            }
            TextUtils.isEmpty(tietUserName) -> {
                til_username.error = getString(R.string.text_required_field)
                til_email.error = null
                til_password_ca.error = null
                til_password_confirm.error = null
            }
            TextUtils.isEmpty(tietEamil) -> {
                til_username.error = null
                til_email.error = getString(R.string.text_required_field)
                til_password_ca.error = null
                til_password_confirm.error = null
            }
            !Tools.isValidEmail(tietEamil) -> {
                til_username.error = null
                til_email.error = getString(R.string.text_invalid_email)
                til_password_ca.error = null
                til_password_confirm.error = null
            }
            TextUtils.isEmpty(tietPassword) -> {
                til_username.error = null
                til_email.error = null
                til_password_ca.error = getString(R.string.text_required_field)
                til_password_confirm.error = null
            }
            TextUtils.isEmpty(tietConfirmPassword) -> {
                til_username.error = null
                til_email.error = null
                til_password_ca.error = null
                til_password_confirm.error = getString(R.string.text_required_field)
            }
            tietPassword != tietConfirmPassword -> {
                til_username.error = null
                til_email.error = null
                til_password_ca.error = getString(R.string.text_password_not_match)
                til_password_confirm.error = getString(R.string.text_password_not_match)
            }
            else -> {
                til_username.error = null
                til_email.error = null
                til_password_ca.error = null
                til_password_confirm.error = null
                //btnSignUp.isEnabled = false
                signUp()
            }
        }
    }

    /**
     * Save the information in Backend
     */
    private fun signUp() {
        user.username = tiet_user_name.text.toString()
        user.email = tiet_email.text.toString()
        user.setPassword(tiet_password_ca.text.toString())
        user.signUpInBackground { e ->
            if (e == null) {
                Log.i(SIGN_UP, SUCCES_MESSAGE)
                redirectLogin()
            } else {
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Automatically when the process of create a new account is complete
     * the flow of the app is automatically redirect to LoginActivity
     */
    private fun redirectLogin() {
        // Inflates the dialog with custom view
        val dialogView  = layoutInflater.inflate(R.layout.layout_custom_alert_builder_signup, null)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(dialogView)
        val alertDialog = dialog.show()
        dialogView.btn_alert_succes.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
    }

    /**
     * Bring support for the back Button in AppBar
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
}
