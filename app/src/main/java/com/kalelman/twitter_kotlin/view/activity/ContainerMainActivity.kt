package com.kalelman.twitter_kotlin.view.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.R.id.txv_toolbar
import com.kalelman.twitter_kotlin.commons.TWEET
import com.kalelman.twitter_kotlin.commons.TWEET_FAILED
import com.kalelman.twitter_kotlin.commons.TWEET_SEND
import com.kalelman.twitter_kotlin.commons.USERNAME
import com.kalelman.twitter_kotlin.view.fragment.ContentFragmentFeed
import com.kalelman.twitter_kotlin.view.fragment.ContentFragmentFollowers
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.SaveCallback
import kotlinx.android.synthetic.main.layout_custom_alert_logout.view.*
import kotlinx.android.synthetic.main.layout_custom_alert_twitter.view.*

class ContainerMainActivity : ToolBar() {

    /*@BindView(R.id.txv_toolbar)
    internal var txvToolBar: TextView? = null
    @BindView(R.id.drawer)
    internal var drawerLayout: DrawerLayout? = null
    @BindView(R.id.navigation_view)
    internal var navigationView: NavigationView? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_container_main)
        setTitleActionBar("")
        showMainScreen()
        //setTitleActionBar("")
        setText()
    }

    /**
     * Inflate the menu of the ToolBar
     * @param menu
     * @return boolean true
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Custo menu for selecting the options in Twitter Clone
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tweet)
            sendTweet()
        else if (item.itemId == R.id.feed)
            showFeedUser()
        else if (item.itemId == R.id.log_out_option)
            userLogOut()
        return super.onOptionsItemSelected(item)
    }

    private fun sendTweet() {
        // Inflates the dialog with custom view
        val dialogView = layoutInflater.inflate(R.layout.layout_custom_alert_twitter, null)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(dialogView)
        val alertDialog = dialog.show()

        dialogView.btn_send_tweet.setOnClickListener {
            val edtSendTweet: EditText = findViewById(R.id.edt_tweet)

            val tweet = ParseObject(TWEET)
            tweet.put(TWEET, edtSendTweet.text.toString())
            tweet.put(USERNAME, ParseUser.getCurrentUser().username)

            tweet.saveInBackground { e ->
                if (e == null) {
                    alertDialog.dismiss()
                    Toast.makeText(this@ContainerMainActivity, TWEET_SEND, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@ContainerMainActivity, TWEET_FAILED, Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialogView.btn_cancel_tweet.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun setText() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_followers)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_container_main
    }

    private fun showMainScreen() {
        val fragmentFollowers = ContentFragmentFollowers()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment, fragmentFollowers)
        ft.addToBackStack(null)
        ft.commit()
    }

    /**
     * Method for load the Feed for the User
     */
    private fun showFeedUser() {
        //txvToolBar.setText(resources.getText(R.string.text_feed))
        val fragmentFeed = ContentFragmentFeed()
        val ff = supportFragmentManager.beginTransaction()
        ff.replace(R.id.fragment, fragmentFeed)
        ff.addToBackStack(null)
        ff.commit()
    }

    override fun onBackPressed() {
        userLogOut()
    }

    private fun userLogOut() {
        // Inflates the dialog with custom view
        val dialogView = layoutInflater.inflate(R.layout.layout_custom_alert_logout, null)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(dialogView)
        val alertDialog = dialog.show()

        dialogView.btn_alert_accept.setOnClickListener {
            ParseUser.logOut()
            alertDialog.dismiss()
            finish()
        }

        dialogView.btn_alert_cancel.setOnClickListener {
            alertDialog.dismiss()
        }

    }
}
