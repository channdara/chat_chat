package com.mastertipsy.chat_chat.helper

import android.content.Context

class SharedPreferencesHelper {
    companion object {
        private val preferencesName = "chat_chat_preferences"
        private val privateMode = Context.MODE_PRIVATE
        private val appToken = "token"

        fun saveToken(context: Context, token: String) {
            val editor = context.getSharedPreferences(preferencesName, privateMode).edit()
            editor.putString(appToken, "Token $token").apply()
        }

        fun loadToken(context: Context): String? {
            val pref = context.getSharedPreferences(preferencesName, privateMode)
            return pref.getString(appToken, null)
        }

        fun removeToken(context: Context) {
            val editor = context.getSharedPreferences(preferencesName, privateMode).edit()
            editor.remove(appToken).apply()
        }
    }
}