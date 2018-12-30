package com.kalelman.twitter_kotlin.commons

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.util.regex.Pattern

/**
 * @author Erick Rojas Perez
 * @date December/22/2018
 * @description Class used to manage the methods used throughout the App.
 */
class Tools {
    /**
     * Class constructor.
     */
    init {
        className = javaClass.toString()
    }

    companion object {

        private lateinit var className: String

        /**
         * Validate an email
         * @param email string with mail to validate.
         * @return True or False.
         */
        fun isValidEmail(email: String): Boolean {

            val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)

            return matcher.matches()
        }

        /**
         * Hides the current open keyboard
         *
         * @param activity the activity
         */
        fun hideKeyboard(activity: Activity) {
            try {
                val imm = activity.getSystemService(
                        INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(className, e.message)
            }
        }
    }
}
