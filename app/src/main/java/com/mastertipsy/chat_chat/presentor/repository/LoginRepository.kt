package com.mastertipsy.chat_chat.presentor.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.presentor.view.LoginView

class LoginRepository(context: Context, private val view: LoginView) {
    private val auth = FirebaseAuth.getInstance()
    private val dialog = AlertUtil.progressDialog(context)
    private val unknownError = context.getString(R.string.error_unknown_error)

    fun login(email: String, password: String) {
        dialog.show()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
            if (login.isSuccessful) {
                auth.currentUser?.let { view.onLoginSuccess() }
                dialog.dismiss()
                return@addOnCompleteListener
            }
            view.onError(login.exception?.message ?: unknownError)
            dialog.dismiss()
        }
    }
}