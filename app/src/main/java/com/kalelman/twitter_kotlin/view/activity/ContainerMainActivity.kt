package com.kalelman.twitter_kotlin.view.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TextInputEditText
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.TWEET
import com.kalelman.twitter_kotlin.commons.TWEET_FAILED
import com.kalelman.twitter_kotlin.commons.TWEET_SEND
import com.kalelman.twitter_kotlin.commons.USERNAME
import com.kalelman.twitter_kotlin.view.fragment.ContentFragmentFeed
import com.kalelman.twitter_kotlin.view.fragment.ContentFragmentFollowers
import com.kalelman.twitter_kotlin.view.fragment.ContentFragmentUserFeed
import com.parse.ParseObject
import com.parse.ParseUser
import com.roughike.bottombar.BottomBar
import kotlinx.android.synthetic.main.layout_custom_alert_logout.view.*
import kotlinx.android.synthetic.main.layout_custom_alert_twitter.view.*

class ContainerMainActivity : ToolBar() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleActionBar("")
        showMainScreen()
        setTextTranslate()
        setUpNavigationDrawer()
        setUpBottomBar()
    }

    private fun setUpBottomBar() {
        val bottomBar = findViewById<BottomBar>(R.id.bottombar)

        bottomBar.setOnTabSelectListener { tabId ->
            when (tabId) {
                R.id.followers -> {
                    showMainScreen()
                }
                R.id.feed -> {
                    showFeed()

                }
                R.id.your_feed -> {
                    showUserFeed()
                }
            }
        }
    }

    private fun setUpNavigationDrawer() {
        val toolBar: Toolbar = this.findViewById(R.id.toolbar)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawer)
        val navigationView : NavigationView = findViewById(R.id.navigation_view)

        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.text_openDrawer, R.string.text_closeDrawer) {
            override fun onDrawerOpened(drawerView: View) {
                drawerView.let { super.onDrawerOpened(it) }
            }

            override fun onDrawerClosed(drawerView: View) {
                drawerView.let { super.onDrawerClosed(it) }
            }
        }

        drawerLayout.setDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.followers -> {
                    showMainScreen()
                    true
                }

                R.id.user_feed -> {
                    showFeed()
                    true
                }

                R.id.log_out -> {
                    userLogOut()
                    true
                }

                else -> false
            }
        }
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
        when {
            item.itemId == R.id.tweet -> sendTweet()

            item.itemId == R.id.feed -> showFeed()

            item.itemId == R.id.log_out_option -> userLogOut()
        }
        return super.onOptionsItemSelected(item)
    }

    fun sendTweet() {
        // Inflates the dialog with custom view
        val dialogView = layoutInflater.inflate(R.layout.layout_custom_alert_twitter, null)
        val tietSendTweet : TextInputEditText = dialogView.findViewById(R.id.tiet_tweet)
        val dialog = AlertDialog.Builder(this)
        dialog.setView(dialogView)
        val alertDialog = dialog.show()

        dialogView.btn_send_tweet.setOnClickListener {
            if (!TextUtils.isEmpty(tietSendTweet.text)) {
                tietSendTweet.error = null
                val tweet = ParseObject(TWEET)
                tweet.put(TWEET, tietSendTweet.text.toString())
                tweet.put(USERNAME, ParseUser.getCurrentUser().username)

                tweet.saveInBackground { e ->
                    if (e == null) {
                        alertDialog.dismiss()
                        Toast.makeText(this@ContainerMainActivity, TWEET_SEND, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ContainerMainActivity, TWEET_FAILED, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                tietSendTweet.error = getString(R.string.text_required_field)
            }
        }

        dialogView.btn_cancel_tweet.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    private fun setTextTranslate() {
        //val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        //txvToolBar.text = getString(R.string.text_followers)
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_container_main
    }

    private fun showMainScreen() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_followers)
        val fragmentFollowers = ContentFragmentFollowers()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment, fragmentFollowers)
        ft.addToBackStack(null)
        ft.commit()
    }

    /**
     * Method for load the Feed for the User
     */
    private fun showFeed() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_general_feed)

        val fragmentFeed = ContentFragmentFeed()
        val ff = supportFragmentManager.beginTransaction()
        ff.replace(R.id.fragment, fragmentFeed)
        ff.addToBackStack(null)
        ff.commit()
    }

    private fun showUserFeed() {
        val txvToolBar: TextView = findViewById(R.id.txv_toolbar)
        txvToolBar.text = getString(R.string.text_feed)

        val fragmentUserFeed = ContentFragmentUserFeed()
        val fuf = supportFragmentManager.beginTransaction()
        fuf.replace(R.id.fragment, fragmentUserFeed)
        fuf.addToBackStack(null)
        fuf.commit()
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
