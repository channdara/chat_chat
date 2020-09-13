package com.mastertipsy.chat_chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.model.ChatRoom
import com.mastertipsy.chat_chat.model.User
import kotlinx.android.synthetic.main.item_view_chat_room_list.view.*

class ChatRoomListAdapter(option: FirestoreRecyclerOptions<ChatRoom>, private val user: User) :
    FirestoreRecyclerAdapter<ChatRoom, ChatRoomListAdapter.ChatViewHolder>(option) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_chat_room_list, parent, false)
        return ChatViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: ChatRoom) {
        holder.bind(model)
    }

    inner class ChatViewHolder(private val view: View, private val context: Context) :
        RecyclerView.ViewHolder(view) {

        fun bind(room: ChatRoom) {
            Glide.with(context).load(room.image).into(view.iv_room_image)
            view.tv_room_name.text = room.name
            view.tv_room_last_message.text = room.lastMessage
            view.tv_room_last_active.text = room.lastActive.toDate().toString()
            if (room.isRead.toString().contains(user.userID)) {
                view.card_view_chat_room_item.setCardBackgroundColor(context.getColor(android.R.color.white))
            } else {
                view.card_view_chat_room_item.setCardBackgroundColor(context.getColor(R.color.color_secondary))
            }
        }
    }
}