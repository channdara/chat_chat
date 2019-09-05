package com.mastertipsy.chat_chat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mastertipsy.chat_chat.R

class ChatRoomListActivity : AppCompatActivity() {
    companion object {
        fun startNewTaskClearTop(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun start(context: Context) {
            context.startActivity(Intent(context, ChatRoomListActivity::class.java))
            (context as Activity).finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)
    }
}
