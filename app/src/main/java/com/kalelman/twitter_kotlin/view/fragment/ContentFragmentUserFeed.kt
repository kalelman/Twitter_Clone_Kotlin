package com.kalelman.twitter_kotlin.view.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.*
import com.kalelman.twitter_kotlin.view.activity.ContainerMainActivity
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import java.util.ArrayList
import java.util.HashMap

class ContentFragmentUserFeed : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_content_user_feed, container, false)

        setupFloatingActionButton(view)
        setupListUserFeed(view)

        return view
    }

    private fun setupListUserFeed(view: View) {
        val listViewFeed = view.findViewById<ListView>(R.id.listView_user_feed)

        val tweetData = ArrayList<Map<String, String>>()

        val query = ParseQuery.getQuery<ParseObject>(TWEET)
        query.whereContainedIn(USERNAME, ParseUser.getCurrentUser().getList<String>(IS_FOLLOWING))
        query.orderByAscending(CREATED_AT)
        query.limit = 20

        query.findInBackground { objects, e ->
            if (e == null) {
                for (tweet in objects) {
                    val tweetInfo = HashMap<String, String>()
                    tweet.getString(TWEET)?.let { tweetInfo.put(CONTENT, it) }
                    tweet.getString(USERNAME)?.let { tweetInfo.put(USERNAME, it) }
                    tweetData.add(tweetInfo)
                }

                val simpleAdapter = SimpleAdapter(activity,
                        tweetData,
                        android.R.layout.simple_list_item_2,
                        arrayOf(CONTENT, USERNAME),
                        intArrayOf(android.R.id.text1, android.R.id.text2))

                listViewFeed.adapter = simpleAdapter
            }
        }



    }

    private fun setupFloatingActionButton(view: View) {
        val fab : FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            (activity as ContainerMainActivity).sendTweet()
        }
    }
}