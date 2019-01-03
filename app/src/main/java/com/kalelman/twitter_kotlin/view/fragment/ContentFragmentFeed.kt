package com.kalelman.twitter_kotlin.view.fragment

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.language.v1.Document
import com.google.cloud.language.v1.LanguageServiceClient
import com.google.cloud.language.v1.LanguageServiceSettings
import com.kalelman.twitter_kotlin.R
import com.kalelman.twitter_kotlin.commons.*
import com.kalelman.twitter_kotlin.view.activity.ContainerMainActivity
import com.parse.*
import kotlinx.android.synthetic.main.fragment_content_feed.*
import kotlinx.android.synthetic.main.layout_custom_alert_sentiment.view.*
import java.io.IOException
import java.util.ArrayList
import java.util.HashMap

class ContentFragmentFeed : Fragment() {

    private var mLanguageClient: LanguageServiceClient? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_content_feed, container, false)

        setupFloatingActionButton(view)

        prepareGoogleApi()

        val listViewFeed = view.findViewById<ListView>(R.id.listView_feed)

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

        /**
         *  Listener for the row selected by the user and get the Tweet text
         */
        listViewFeed.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val textTweet = view.findViewById<TextView>(android.R.id.text1)
            analyzeSentiment(textTweet.text.toString())
        }

        return view
    }

    private fun setupFloatingActionButton(view: View) {
        val fab : FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            (activity as ContainerMainActivity).sendTweet()
        }
    }

    private fun prepareGoogleApi() {
        // create the language client
        try {
            // NOTE: The line below uses an embedded credential (res/raw/credential.json).
            //       You should not package a credential with real application.
            //       Instead, you should get a credential securely from a server.
            mLanguageClient = LanguageServiceClient.create(
                    LanguageServiceSettings.newBuilder()
                            .setCredentialsProvider {
                                GoogleCredentials.fromStream(activity?.resources?.openRawResource(R.raw.credential))
                            }
                            .build())
        } catch (e: IOException) {
            Log.e("ERROR-->", e.toString())
            throw IllegalStateException("Unable to create a language client", e)
        }
    }

    private fun analyzeSentiment(text: String) {

        val doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build()
        // Analyze the sentiment of the text
        val sentiment = mLanguageClient?.analyzeSentiment(doc)?.documentSentiment
        showAlertSentiment(sentiment?.score)
    }

    private fun showAlertSentiment(sentiment: Float?) {
        val dialogView = layoutInflater.inflate(R.layout.layout_custom_alert_sentiment, null)
        val layout : ConstraintLayout = dialogView.findViewById(R.id.background_alert)
        val imvSentiment : ImageView = dialogView.findViewById(R.id.imv_emoticon)
        val txvSentiment : TextView = dialogView.findViewById(R.id.txv_sentiment_message)

        when {
            sentiment!! > 0.25 -> {
                //show Alert Sentiment Positive
                layout.setBackgroundColor(resources.getColor(R.color.score_positive))
                imvSentiment.setImageResource(R.drawable.ic_emoji_positive)
                txvSentiment.text = resources.getText(R.string.text_sentiment_positive)
            }
            sentiment!! > -0.75 -> {
                //show Alert Sentiment Neutral
                layout.setBackgroundColor(resources.getColor(R.color.score_neutral))
                imvSentiment.setImageResource(R.drawable.ic_emoji_neutral)
                txvSentiment.text = resources.getText(R.string.text_sentiment_neutral)
            }
            else -> {
                //Show Alert Sentiment Negative
                layout.setBackgroundColor(resources.getColor(R.color.score_negative))
                imvSentiment.setImageResource(R.drawable.ic_emoji_negative)
                txvSentiment.text = resources.getText(R.string.text_sentiment_negative)
            }
        }

        val dialog = activity?.let { AlertDialog.Builder(it) }
        dialog?.setView(dialogView)
        val alertDialog = dialog?.show()

        dialogView.btn_sentiment.setOnClickListener { alertDialog?.dismiss()}

    }

    /**
     * Shutdown the Google API
     */
    override fun onDestroy() {
        super.onDestroy()
        // shutdown the connection
        mLanguageClient?.shutdown()
    }

}