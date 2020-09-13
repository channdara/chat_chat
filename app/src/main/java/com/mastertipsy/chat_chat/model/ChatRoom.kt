package com.mastertipsy.chat_chat.model

import com.google.firebase.Timestamp

class ChatRoom(
    var roomID: String = "",
    var image: String = "",
    var isRead: List<String> = emptyList(),
    var lastActive: Timestamp = Timestamp.now(),
    var lastMessage: String = "",
    var members: List<String> = emptyList(),
    var name: String = "",
    var type: Int = 0
) {

    companion object {
        const val collection = "ChatRoom"
        const val image = "image"
        const val lastActive = "lastActive"
        const val lastMessage = "lastMessage"
        const val members = "members"
        const val name = "name"
        const val isRead = "isRead"
        const val type = "type"

        fun fromMap(roomID: String, map: Map<String, Any>): ChatRoom {
            return ChatRoom(
                roomID,
                map[image] as String,
                map[isRead] as List<String>,
                map[lastActive] as Timestamp,
                map[lastMessage] as String,
                map[members] as List<String>,
                map[name] as String,
                map[type] as Int
            )
        }
    }
}