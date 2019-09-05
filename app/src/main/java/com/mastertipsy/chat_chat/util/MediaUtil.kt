package com.mastertipsy.chat_chat.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class MediaUtil {
    companion object {
        fun getCompressedUri(context: Context, bitmap: Bitmap): Uri {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "", "")
            return Uri.parse(path)
        }
    }
}