package com.mastertipsy.chat_chat.util

import java.text.SimpleDateFormat
import java.util.*

class TextUtil {
    companion object {
        fun getCurrentDateTime(): String =
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSSSSS", Locale.getDefault()).format(Date())
    }
}