package com.mastertipsy.chat_chat.presentor.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.helper.SharedPrefHelper
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.view.LoginView
import com.mastertipsy.chat_chat.util.AlertUtil

class LoginRepository(private val context: Context, private val view: LoginView) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val dialog = AlertUtil.progressDialog(context)
    private val unknownError = context.getString(R.string.error_unknown_error)

    fun login(email: String, password: String) {
        dialog.show()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveUserID(email)
                return@addOnCompleteListener
            }
            view.onError(task.exception?.message ?: unknownError)
            dialog.dismiss()
        }
    }

    private fun saveUserID(email: String) {
        firestore.collection(User.collection).whereEqualTo(User.emailAddress, email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.documents?.get(0)?.id ?: ""
                    SharedPrefHelper.saveUserID(context, uid)
                    view.onLoginSuccess(email)
                    dialog.dismiss()
                    return@addOnCompleteListener
                }
                view.onError(task.exception?.message ?: unknownError)
                dialog.dismiss()
            }
    }
}