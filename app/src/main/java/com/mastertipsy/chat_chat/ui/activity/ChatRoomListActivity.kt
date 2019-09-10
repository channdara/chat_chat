package com.mastertipsy.chat_chat.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mastertipsy.chat_chat.R
import kotlinx.android.synthetic.main.activity_chat_room_list.*

class ChatRoomListActivity : AppCompatActivity() {
    companion object {
        fun startNewTaskClearTask(context: Context) {
            val intent = Intent(context, ChatRoomListActivity::class.java)
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
        logout_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginActivity.startNewTaskClearTask(this)
        }
    }
}
