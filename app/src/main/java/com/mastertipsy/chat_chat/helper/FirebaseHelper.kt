package com.mastertipsy.chat_chat.helper

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.util.MediaUtil
import com.mastertipsy.chat_chat.util.TextUtil

class FirebaseHelper(private val context: Context) {
    private val storageInstance = FirebaseStorage.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storagePath = "UserProfile"

    fun compressImageAndUpload(bitmap: Bitmap): String {
        val fileUri = MediaUtil.getCompressedUri(context, bitmap)
        val imageName = "$storagePath/${TextUtil.getCurrentDateTime()}"
        val reference = storageInstance.reference.child(imageName)
        return reference.putFile(fileUri).result.metadata?.path ?: ""
    }

    fun addUserToFirestore(user: User) {
        firestoreInstance.collection(User.collection).add(user.toHashMap())
    }
}