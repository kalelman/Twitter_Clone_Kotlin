package com.kalelman.twitter_kotlin.view.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import com.kalelman.twitter_kotlin.commons.SIGN_UP
import com.kalelman.twitter_kotlin.commons.SUCCES_MESSAGE
import com.kalelman.twitter_kotlin.commons.Tools
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.layout_custom_alert_builder_signup.*
import com.parse.ParseUser
import kotlinx.android.synthetic.main.layout_custom_alert_builder_signup.view.*


class CreateAccountActivity : ToolBar() {

    @BindView(R.id.til_username)
    lateinit var tilUsername: TextInputLayout
    @BindView(R.id.tiet_username)
    lateinit var tietUsername: TextInputEditText
    @BindView(R.id.til_email)
    lateinit var tilEmail: TextInputLayout
    @BindView(R.id.tiet_email)
    lateinit var tietEmail: TextInputEditText
    @BindView(R.id.til_password_ca)
    lateinit var tilPassword: TextInputLayout
    @BindView(R.id.tiet_password_ca)
    lateinit var tietPassword: TextInputEditText
    @BindView(R.id.til_password_confirm)
    lateinit var tilConfirmPassword: TextInputLayout
    @BindView(R.id.tiet_password_confirm)
    lateinit var tietConfirmPassword: TextInputEditText
    @BindView(R.id.btn_sign_up_account)
    lateinit var btnSignUp: Button

    private val user = ParseUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_account)
        setTextTranslate()
        setTitleActionBar("")
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_create_account

    }

    private fun setTextTranslate() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_sign_up)
    }

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
}
