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

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        /*if(getAccessToken() != " " && getRefreshToken() != " ")
        {
            return true
        }*/
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        return sharedPreferences.getString(context.getString(R.string.access_token), " ")

    }

    fun saveAccessToken(accessToken: String) {
        //TODO("Save Access Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(context.getString(R.string.access_token), accessToken)
            commit()
        }
    }

    fun clearAccessToken() {
        //TODO("Clear Access Token")
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.access_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }

    fun getRefreshToken(): String? {
        //TODO("Get Refresh Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        return sharedPreferences.getString(context.getString(R.string.refresh_token), " ")
    }

    fun saveRefreshToken(refreshToken: String) {
        //TODO("Save Refresh Token")

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            putString(context.getString(R.string.refresh_token), refreshToken)
            commit()
        }

    }

    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.refresh_token), Context.MODE_PRIVATE)
        with (sharedPreferences.edit()) {
            clear()
            commit()
        }
    }

}