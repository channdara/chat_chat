package com.mastertipsy.chat_chat.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mastertipsy.chat_chat.R

class ChatRoomListActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, ChatRoomListActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)
    }
}
