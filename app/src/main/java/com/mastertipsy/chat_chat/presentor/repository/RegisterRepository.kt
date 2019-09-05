package com.mastertipsy.chat_chat.presentor.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.helper.FirebaseHelper
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.presentor.view.RegisterView

class RegisterRepository(context: Context, private val view: RegisterView) {
    private val firebaseHelper: FirebaseHelper = FirebaseHelper(context)
    private val auth = FirebaseAuth.getInstance()
    private val dialog = AlertUtil.progressDialog(context)
    private val unknownError = context.getString(R.string.error_unknown_error)

    fun createUserWithEmailAndPassword(user: User) {
        dialog.show()
        auth.createUserWithEmailAndPassword(user.emailAddress, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseHelper.addUserToFirestore(user)
                    auth.currentUser?.let { sendEmailVerification(it) }
                    return@addOnCompleteListener
                }
                view.onError(task.exception?.message ?: unknownError)
                dialog.dismiss()
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view.onRegisterSuccess()
                dialog.dismiss()
                return@addOnCompleteListener
            }
            view.onError(task.exception?.message ?: unknownError)
            dialog.dismiss()
        }
    }
}