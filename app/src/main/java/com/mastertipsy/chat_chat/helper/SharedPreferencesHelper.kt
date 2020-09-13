package com.mastertipsy.chat_chat.helper

import android.content.Context

class SharedPrefHelper {
    companion object {
        private const val PRIVATE = Context.MODE_PRIVATE
        private const val PREF_NAME = "CHAT_CHAT"
        private const val USER_ID = "USER_ID"

        fun saveUserID(context: Context, uid: String) {
            val pref = context.getSharedPreferences(PREF_NAME, PRIVATE)
            pref.edit().putString(USER_ID, uid).apply()
        }

        fun loadUserID(context: Context): String {
            val pref = context.getSharedPreferences(PREF_NAME, PRIVATE)
            return pref.getString(USER_ID, null) ?: ""
        }

        fun removeUserID(context: Context) {
            val pref = context.getSharedPreferences(PREF_NAME, PRIVATE)
            pref.edit().remove(USER_ID).apply()
        }

        fun removeAll(context: Context) {
            val pref = context.getSharedPreferences(PREF_NAME, PRIVATE)
            pref.edit().clear().apply()
        }
    }
}