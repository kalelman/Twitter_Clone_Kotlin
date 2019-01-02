package com.kalelman.twitter_kotlin.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.IS_FOLLOWING
import com.kalelman.twitter_kotlin.commons.USERNAME
import com.parse.ParseUser
import java.util.ArrayList


class ContentFragmentFollowers : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_content_followers, container, false)

        val listView = view.findViewById<ListView>(R.id.listView_followers)
        listView.choiceMode = AbsListView.CHOICE_MODE_MULTIPLE
        val users = ArrayList<String>()
        val adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_checked, users)
        listView.adapter = adapter as ListAdapter?

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val checkedTextView = view as CheckedTextView
            if (checkedTextView.isChecked) {
                Log.i("Info", "Checked!")
                ParseUser.getCurrentUser().add(IS_FOLLOWING, users[i])
            } else {
                Log.i("Info", "NOT Checked!")
                ParseUser.getCurrentUser().getList<String>(IS_FOLLOWING)?.remove(users[i])
                val tempUsers = ParseUser.getCurrentUser().getList<String>(IS_FOLLOWING)
                ParseUser.getCurrentUser().remove(IS_FOLLOWING)
                ParseUser.getCurrentUser().put(IS_FOLLOWING, tempUsers!!)
            }

            ParseUser.getCurrentUser().saveInBackground()
        }

        val query = ParseUser.getQuery()

        query.whereNotEqualTo(USERNAME, ParseUser.getCurrentUser().username)

        query.findInBackground { objects, e ->
            if (e == null && objects.size > 0) {
                for (user in objects) {
                    users.add(user.username)
                }

                adapter.notifyDataSetChanged()

                for (username in users) {
                    if (ParseUser.getCurrentUser().getList<String>(IS_FOLLOWING)?.contains(username)!!) {
                        listView?.setItemChecked(users.indexOf(username), true)
                    }
                }
            }
        }

        return view
    }
}