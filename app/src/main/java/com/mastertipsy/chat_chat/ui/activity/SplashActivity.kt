package com.mastertipsy.chat_chat.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mastertipsy.chat_chat.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            LoginActivity.start(this)
            return
        }
        ChatRoomListActivity.start(this)
    }
}
