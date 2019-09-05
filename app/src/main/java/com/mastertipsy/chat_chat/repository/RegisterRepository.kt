package com.mastertipsy.chat_chat.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.helper.FirebaseHelper
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.view.RegisterView

class RegisterRepository(private val context: Context, private val view: RegisterView) {
    private val firebaseHelper: FirebaseHelper = FirebaseHelper(context)
    private val auth = FirebaseAuth.getInstance()
    private val unknownError = context.getString(R.string.error_unknown_error)
    private val dialog = AlertUtil.progressDialog(context)

    fun register(user: User) {
        dialog.show()
        auth.createUserWithEmailAndPassword(user.emailAddress, user.password)
            .addOnCompleteListener { register ->
                if (register.isSuccessful) {
//                    user.profileImage = firebaseHelper.compressImageAndUpload(image)
                    firebaseHelper.addUserToFirestore(user)
                    auth.currentUser?.let { verifyEmail(it) }
                    return@addOnCompleteListener
                }
                val message = register.exception?.message ?: unknownError
                view.onError(message)
                dialog.dismiss()
            }
    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification().addOnCompleteListener { verify ->
            if (verify.isSuccessful) {
                view.onRegisterSuccess(user)
                dialog.dismiss()
                return@addOnCompleteListener
            }
            val message = verify.exception?.message ?: unknownError
            view.onError(message)
            dialog.dismiss()
        }
    }
}