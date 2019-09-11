package com.mastertipsy.chat_chat.presentor.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.view.RegisterView
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.util.TextUtil

class RegisterRepository(context: Context, private val view: RegisterView) {
    private val auth = FirebaseAuth.getInstance()
    private val dialog = AlertUtil.progressDialog(context)
    private val storageInstance = FirebaseStorage.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val unknownError = context.getString(R.string.error_unknown_error)
    private val storagePath = "UserProfile"

    fun registerUser(user: User, image: Uri?) {
        dialog.show()
        auth.createUserWithEmailAndPassword(user.emailAddress, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                    uploadUserProfile(user, image)
                    return@addOnCompleteListener
                }
                view.onError(task.exception?.message ?: unknownError)
                dialog.dismiss()
            }
    }

    private fun uploadUserProfile(user: User, image: Uri?) {
        if (image == null) {
            addUserToFirestore(user)
            return
        }
        val imageName = "$storagePath/${TextUtil.getCurrentDateTime()}"
        storageInstance.reference.child(imageName).putFile(image)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageInstance.reference.child(imageName)
                        .downloadUrl.addOnCompleteListener { uri ->
                        user.profileImage = uri.result.toString()
                        addUserToFirestore(user)
                    }
                    return@addOnCompleteListener
                }
                view.onError(task.exception?.message ?: unknownError)
                dialog.dismiss()
            }
    }

    private fun addUserToFirestore(user: User) {
        firestoreInstance.collection(User.collection).add(user.toHashMap())
            .addOnCompleteListener { task ->
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