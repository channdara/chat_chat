package com.mastertipsy.chat_chat.presentor.view

import com.mastertipsy.chat_chat.model.ChatRoom
import com.mastertipsy.chat_chat.model.User

interface ChatRoomListView : BaseView {
    fun onFetchUserSuccess(user: User)
    fun onFetchRoomsSuccess(rooms: List<ChatRoom>)
}