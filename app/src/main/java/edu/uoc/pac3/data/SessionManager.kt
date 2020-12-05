package edu.uoc.pac3.data

import android.content.Context
import android.util.Log
import edu.uoc.pac3.R
import io.ktor.utils.io.concurrent.shared

/**
 * Created by alex on 06/09/2020.
 * Modified by albert on 01/12/2020
 */

class SessionManager(private val context: Context) {

    private val preferencesKey: String = context.getString(R.string.preference_file_key)
    private val refreshTokenKey: String = context.getString(R.string.refresh_token_token_key)
    private val accessTokenKey: String = context.getString(R.string.access_token_key)

    fun isUserAvailable(): Boolean {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return false
        return sharedPreferences.contains(refreshTokenKey) && sharedPreferences.contains(accessTokenKey)
    }

    fun getAccessToken(): String? {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(accessTokenKey, "")
    }

    fun saveAccessToken(accessToken: String) {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return
        with (sharedPreferences.edit()) {
            putString(accessTokenKey, accessToken)
            commit()
        }
    }

    fun clearAccessToken() {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return
        with (sharedPreferences.edit()) {
            remove(accessTokenKey)
            commit()
        }
    }

    fun getRefreshToken(): String? {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE)
        return sharedPreferences?.getString(refreshTokenKey, "")
    }

    fun saveRefreshToken(refreshToken: String) {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return
        with (sharedPreferences.edit()) {
            putString(refreshTokenKey, refreshToken)
            commit()
        }
    }

    fun clearRefreshToken() {
        var sharedPreferences = context.getSharedPreferences(preferencesKey, Context.MODE_PRIVATE) ?: return
        with (sharedPreferences.edit()) {
            remove(refreshTokenKey)
            commit()
        }
    }

    fun clearUserData() {
        clearRefreshToken()
        clearAccessToken()
    }

}

