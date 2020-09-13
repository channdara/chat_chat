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
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.listener.RecyclerOnClickListener
import kotlinx.android.synthetic.main.item_view_user_list.view.*

class UserListAdapter(
    option: FirestoreRecyclerOptions<User>,
    private val onClick: RecyclerOnClickListener<User>
) : FirestoreRecyclerAdapter<User, UserListAdapter.UserViewHolder>(option) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_user_list, parent, false)
        return UserViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        holder.bind(model)
    }

    inner class UserViewHolder(private val view: View, private val context: Context) :
        RecyclerView.ViewHolder(view) {

        fun bind(user: User) {
            Glide.with(context).load(user.profileImage).into(view.iv_user_image)
            view.tv_username.text = user.username
            itemView.setOnClickListener { onClick.onClick(user) }
        }
    }
}