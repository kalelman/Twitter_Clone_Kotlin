package com.kalelman.twitter_kotlin.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.kalelman.twitter_kotlin.R

abstract class ToolBar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        val toolbar:Toolbar = findViewById(R.id.toolbar)

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow)
        }
    }

    /**
     * Get resource from the Layout
     * @return the layout resource
     */
    abstract fun getLayoutResource(): Int

    /**
     * Set the Title of ActionBar
     * @param titleActionBar
     */
    fun setTitleActionBar(titleActionBar: String) {
        supportActionBar!!.title = titleActionBar
    }

}