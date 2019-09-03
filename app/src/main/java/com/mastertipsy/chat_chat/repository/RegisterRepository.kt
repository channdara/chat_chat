package com.mastertipsy.chat_chat.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.view.RegisterView

class RegisterRepository(private val view: RegisterView) {
    private val unknownError = "Unknown Error!"
    private val auth = FirebaseAuth.getInstance()

    fun register(user: User) {
        auth.createUserWithEmailAndPassword(user.emailAddress, user.password)
            .addOnCompleteListener { register ->
                if (register.isSuccessful) {
                    verifyEmail(auth.currentUser)
                    return@addOnCompleteListener
                }
                view.onError(register.exception?.message ?: unknownError)
            }
    }

    private fun verifyEmail(user: FirebaseUser?) {
        if (user == null) {
            view.onError("Something when wrong when trying to send verification link to your email.")
            return
        }
        user.sendEmailVerification().addOnCompleteListener { verify ->
            if (verify.isSuccessful) {
                view.onRegisterSuccess(user)
                return@addOnCompleteListener
            }
            view.onError(verify.exception?.message ?: unknownError)
        }
    }
}