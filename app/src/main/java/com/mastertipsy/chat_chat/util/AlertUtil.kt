package com.mastertipsy.chat_chat.util

import android.content.Context
import android.widget.Toast
import com.mastertipsy.chat_chat.R

class AlertUtil {
    companion object {
        fun showToast(context: Context, message: String?) {
            val content = message ?: context.getString(R.string.error_unknown_error)
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        }
    }
}