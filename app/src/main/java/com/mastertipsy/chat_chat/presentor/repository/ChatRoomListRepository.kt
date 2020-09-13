package com.mastertipsy.chat_chat.presentor.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.view.ChatRoomListView

class ChatRoomListRepository(context: Context, private val view: ChatRoomListView) {
    private val firestore = FirebaseFirestore.getInstance()
    private val unknownError = context.getString(R.string.error_unknown_error)

    fun getUserByEmail(email: String) {
        firestore.collection(User.collection)
            .whereEqualTo(User.emailAddress, email)
            .get()
            .addOnFailureListener { view.onError(it.message ?: unknownError) }
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val user = User.fromMap(doc.id, doc.data)
                    view.onFetchUserSuccess(user)
                }
            }

    }
}