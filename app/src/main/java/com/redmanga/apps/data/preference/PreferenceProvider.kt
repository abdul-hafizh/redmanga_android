package com.redmanga.apps.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_TOKEN = "key_token"
private const val KEY_IS_LOGIN = "key_is_login"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveToken(savedAt: String) {
        preference.edit().putString(
            KEY_TOKEN,
            savedAt
        ).apply()
    }

    fun getToken(): String? {
        return preference.getString(KEY_TOKEN, null)
    }

    fun setLogin(status: Boolean) {
        preference.edit().putBoolean(
            KEY_IS_LOGIN,
            status
        ).apply()
    }

    fun getLogin(): Boolean? {
        return preference.getBoolean(KEY_IS_LOGIN, false)
    }

}