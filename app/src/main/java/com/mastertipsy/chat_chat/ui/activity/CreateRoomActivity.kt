package com.mastertipsy.chat_chat.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.adapter.UserListAdapter
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.listener.RecyclerOnClickListener

class CreateRoomActivity : AppCompatActivity() {
    companion object {
        const val UID = "UID"

        fun start(context: Context, uid: String) {
            val intent = Intent(context, CreateRoomActivity::class.java)
            intent.putExtra(UID, uid)
            context.startActivity(Intent(context, CreateRoomActivity::class.java))
        }
    }

    private lateinit var adapter: UserListAdapter

    private val currentUserID by lazy { intent.getStringExtra(UID) }

    private val rvUserList by lazy { findViewById<RecyclerView>(R.id.rv_user_list) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creat_room)
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun setupRecyclerView() {
        rvUserList.layoutManager = LinearLayoutManager(this)
        val query = FirebaseFirestore.getInstance().collection(User.collection)
        val option = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()
        adapter = UserListAdapter(option, object : RecyclerOnClickListener<User> {
            override fun onClick(item: User) {
                val userList = listOf(item.userID, currentUserID)
                FirebaseFirestore.getInstance()
                    .collection("ChatRoom")
                    .whereArrayContains("members", userList)
                    .get()
            }
        })
        rvUserList.adapter = adapter
    }
}
