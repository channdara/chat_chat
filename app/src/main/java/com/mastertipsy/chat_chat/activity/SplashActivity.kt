package com.mastertipsy.chat_chat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.helper.SharedPreferencesHelper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = SharedPreferencesHelper.loadToken(this)
        if (token == null) {
            LoginActivity.start(this)
            return
        }
        ChatRoomListActivity.start(this)
    }
}
