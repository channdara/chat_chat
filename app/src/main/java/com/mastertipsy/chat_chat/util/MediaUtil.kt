package com.mastertipsy.chat_chat.util

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MediaUtil {
    companion object {
        fun getCompressedImageUri(contentResolver: ContentResolver, bitmap: Bitmap): Uri {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, ByteArrayOutputStream())
            val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "", "")
            return Uri.parse(path)
        }

        fun uriToBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap? {
            val tmpOption = BitmapFactory.Options()
            tmpOption.inJustDecodeBounds = true
            val stream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(stream, null, tmpOption)
            var tmpWidth = tmpOption.outWidth
            var tmpHeight = tmpOption.outHeight
            var tmpScale = 1
            while (true) {
                if (tmpWidth / 2 < 800 || tmpHeight / 2 < 800) break
                tmpHeight /= 2
                tmpWidth /= 2
                tmpScale *= 2
            }
            val option = BitmapFactory.Options()
            option.inSampleSize = tmpScale
            return BitmapFactory.decodeStream(stream, null, option)
        }

        fun rotateBitmap(context: Context, bitmap: Bitmap, uri: Uri): Bitmap {
            val rotation = getUriOrientation(context, uri)
            if (rotation == -1) return bitmap
            return when (rotation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
                else -> rotate(bitmap, 0f)
            }
        }

        private fun getUriOrientation(context: Context, uri: Uri): Int {
            val projection = arrayOf(MediaStore.Images.ImageColumns.ORIENTATION)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                it.moveToFirst()
                val orientation = it.getInt(0)
                it.close()
                return orientation
            }
            return -1
        }

        fun getUriPath(context: Context, uri: Uri): String {
            var cursor: Cursor? = null
            return try {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val loader =
                    android.content.CursorLoader(context, uri, projection, null, null, null)
                cursor = loader.loadInBackground()
                val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor?.moveToFirst()
                cursor?.getString(index).toString()
            } finally {
                cursor?.close()
            }
        }

        fun createUploadImage(context: Context, bitmap: Bitmap, tmpName: String): File {
            val file = File(context.externalCacheDir, tmpName)
            var fos: FileOutputStream? = null
            try {
                file.createNewFile()
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } finally {
                fos?.let {
                    it.flush()
                    it.close()
                }
            }
            return file
        }

        private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
            val matrix = Matrix().apply { postRotate(degree) }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}