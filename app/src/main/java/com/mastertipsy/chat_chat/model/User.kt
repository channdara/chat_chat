package com.mastertipsy.chat_chat.model

class User(
    var userID: String = "",
    var profileImage: String = "",
    var username: String = "",
    var password: String = "",
    var emailAddress: String = "",
    var phoneNumber: String = ""
) {
    companion object {
        const val collection = "User"
        const val profileImage = "profileImage"
        const val username = "username"
        const val emailAddress = "emailAddress"
        const val phoneNumber = "phoneNumber"

        fun fromMap(id: String, map: Map<String, Any>): User = User(
            id,
            map[profileImage] as String,
            map[username] as String,
            "********",
            map[emailAddress] as String,
            map[phoneNumber] as String
        )
    }

    fun toMap() = hashMapOf(
        User.profileImage to profileImage,
        User.username to username,
        User.emailAddress to emailAddress,
        User.phoneNumber to phoneNumber
    )
}