package com.mastertipsy.chat_chat.model

class User(
    var profileImage: String,
    var username: String,
    var password: String,
    var emailAddress: String,
    var phoneNumber: String
) {
    companion object {
        val collection = "User"
        val profileImage = "profileImage"
        val username = "username"
        val emailAddress = "emailAddress"
        val phoneNumber = "phoneNumber"
    }

    fun toHashMap() = hashMapOf(
        User.profileImage to profileImage,
        User.username to username,
        User.emailAddress to emailAddress,
        User.phoneNumber to phoneNumber
    )
}