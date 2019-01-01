package com.kalelman.twitter_kotlin.view.activity

import android.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.RECOVER_PASSWORD
import com.kalelman.twitter_kotlin.commons.RECOVER_PASSWORD_SUCCES
import com.kalelman.twitter_kotlin.commons.Tools
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_recover_password.*
import kotlinx.android.synthetic.main.layout_custom_alert_builder_reset_password.view.*

class RecoverPasswordActivity : ToolBar() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTextTranslate()
        setTitleActionBar("")
    }

    private fun setTextTranslate() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_change_password)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_recover_password

    }

    fun onResetPasswordClicked(view: View) {
        val edtEnterEmail = edt_enter_email.text.toString()
        Tools.hideKeyboard(this)
        if (TextUtils.isEmpty(edtEnterEmail))
            edt_enter_email.error = getString(R.string.text_required_field)
        else if (!Tools.isValidEmail(edtEnterEmail))
            edt_enter_email.error = getString(R.string.text_invalid_email)
        else {
            edt_enter_email.error = null
            resetPassword(edtEnterEmail)
        }
    }

    private fun resetPassword(edtEnterEmail: String) {
        ParseUser.requestPasswordResetInBackground(edtEnterEmail) { e ->
            if (e == null) {
                Log.i(RECOVER_PASSWORD, RECOVER_PASSWORD_SUCCES)
                redirectLogin()
            } else {
                Toast.makeText(this@RecoverPasswordActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun redirectLogin() {
        // Inflates the dialog with custom view
        val dialogView  = layoutInflater.inflate(R.layout.layout_custom_alert_builder_reset_password, null)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(dialogView)
        val alertDialog = dialog.show()
        dialogView.btn_reset_password.setOnClickListener {
            alertDialog.dismiss()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
