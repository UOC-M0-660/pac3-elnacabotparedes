package edu.uoc.pac3.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import edu.uoc.pac3.R

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    val context = context

    //If user is available
    fun isUserAvailable(): Boolean {
        // TODO: Implement
        if(getAccessToken() != " " && getRefreshToken() != " ")
        {
            return true
        }
        return false
    }

    //Get the access tokens
    fun getAccessToken(): String? {
        // TODO: Implement
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        return sharedPreferences.getString(context.getString(R.string.access_token), " ")

    }

    //Save the access token in the SharedPreferences
    fun saveAccessToken(accessToken: String) {
        //TODO("Save Access Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(context.getString(R.string.access_token), accessToken)
            commit()
        }
    }

    //Remove the access token for the Shared Preferences
    fun clearAccessToken() {
        //TODO("Clear Access Token")
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }

    //Get the refresh token
    fun getRefreshToken(): String? {
        //TODO("Get Refresh Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        return sharedPreferences.getString(context.getString(R.string.refresh_token), " ")
    }

    //Save the refresh token in the Shared Preferences
    fun saveRefreshToken(refreshToken: String) {
        //TODO("Save Refresh Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(context.getString(R.string.refresh_token), refreshToken)
            commit()
        }

    }

    //Remove refresh token from the Shared Preferences
    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }

}