package com.kalelman.twitter_kotlin.model;

import android.app.Application;

import com.kalelman.twitter_kotlin.R;
import com.parse.Parse;
import com.parse.ParseUser;

/**
 * @autor Erick Rojas Perez</br><br>erick_rojas_perez@hotmail.com</br>
 * @date January/03/2019
 * @description Class that establish connexion to the backend
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
        ParseUser.logOut();
    }
}
