package com.mastertipsy.chat_chat.model

import android.media.Image

class User(
    var profile: Image,
    var username: String,
    var password: String,
    var emailAddress: String,
    var phoneNumber: String
)