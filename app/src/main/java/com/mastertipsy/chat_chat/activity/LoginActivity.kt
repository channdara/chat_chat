package com.mastertipsy.chat_chat.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.helper.SharedPreferencesHelper

class LoginActivity : AppCompatActivity() {
    private val btnGotoRegister by lazy { findViewById<AppCompatButton>(R.id.btn_login_register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        checkAppToken()
    }

    private fun checkAppToken() {
        val token = SharedPreferencesHelper.loadToken(this)
        if (token != null) {
            ChatRoomListActivity.start(this)
            return
        }
        setupListener()
    }

    private fun setupListener() {
        btnGotoRegister.setOnClickListener { RegisterActivity.start(this) }
    }
}
