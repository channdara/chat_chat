package com.mastertipsy.chat_chat.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.adapter.ChatRoomListAdapter
import com.mastertipsy.chat_chat.model.ChatRoom
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.repository.ChatRoomListRepository
import com.mastertipsy.chat_chat.presentor.view.ChatRoomListView
import com.mastertipsy.chat_chat.util.AlertUtil

class ChatRoomListActivity : AppCompatActivity(), ChatRoomListView {
    companion object {
        const val EMAIL = "EMAIL"

        fun startNewTaskClearTask(context: Context, email: String) {
            val intent = Intent(context, ChatRoomListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(EMAIL, email)
            context.startActivity(intent)
        }

        fun start(context: Context, email: String) {
            val intent = Intent(context, ChatRoomListActivity::class.java)
            intent.putExtra(EMAIL, email)
            context.startActivity(intent)
            (context as Activity).finish()
        }
    }

    private lateinit var repo: ChatRoomListRepository
    private lateinit var adapter: ChatRoomListAdapter
    private lateinit var user: User

    private val userEmailAddress by lazy { intent.getStringExtra(EMAIL) }

    private val ivMenu by lazy { findViewById<AppCompatImageButton>(R.id.iv_chat_room_list_menu) }
    private val rvChatRoomList by lazy { findViewById<RecyclerView>(R.id.rv_chat_room_list) }
    private val tvTitle by lazy { findViewById<AppCompatTextView>(R.id.tv_chat_room_list_title) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar_chat_room_list) }
    private val fabCreate by lazy { findViewById<FloatingActionButton>(R.id.fab_create_room) }

    private var isAdapterInit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room_list)
        repo = ChatRoomListRepository(this, this)
        repo.getUserByEmail(userEmailAddress)
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        if (!isAdapterInit) return
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    @SuppressLint("SetTextI18n")
    override fun onFetchUserSuccess(user: User) {
        this.user = user
        tvTitle.text = "${getString(R.string.hi)}, ${user.username}"
        setupRecyclerView(user)
        adapter.startListening()
        progressBar.visibility = View.GONE
        rvChatRoomList.visibility = View.VISIBLE
    }

    override fun onFetchRoomsSuccess(rooms: List<ChatRoom>) {}

    override fun onError(message: String) {
        AlertUtil.showAlertDialog(this, getString(R.string.error), message)
    }

    private fun setupListener() {
        ivMenu.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginActivity.startNewTaskClearTask(this)
        }
        fabCreate.setOnClickListener {
            if (!isAdapterInit) return@setOnClickListener
            CreateRoomActivity.start(this, user.userID)
        }
    }

    private fun setupRecyclerView(user: User) {
        rvChatRoomList.layoutManager = LinearLayoutManager(this)
        val query = FirebaseFirestore.getInstance().collection(ChatRoom.collection)
            .whereArrayContains(ChatRoom.members, user.userID)
        val option = FirestoreRecyclerOptions.Builder<ChatRoom>()
            .setQuery(query, ChatRoom::class.java)
            .build()
        adapter = ChatRoomListAdapter(option, user)
        isAdapterInit = true
        rvChatRoomList.adapter = adapter
    }
}
