package com.mastertipsy.chat_chat.view

import com.google.firebase.auth.FirebaseUser

interface RegisterView : BaseView {
    fun onRegisterSuccess(user: FirebaseUser)
}