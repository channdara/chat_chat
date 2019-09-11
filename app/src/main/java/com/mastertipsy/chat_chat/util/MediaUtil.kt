package com.mastertipsy.chat_chat.util

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import androidx.loader.content.CursorLoader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MediaUtil {
    companion object {
        fun compressImage(context: Context, uri: Uri): Bitmap? {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)

            var tmpWidth = options.outWidth
            var tmpHeight = options.outHeight
            var scale = 1

            while (true) {
                if (tmpWidth / 2 < 500 || tmpHeight / 2 < 500)
                    break
                tmpWidth /= 2
                tmpHeight /= 2
                scale *= 2
            }

            val secondOptions = BitmapFactory.Options()
            secondOptions.inSampleSize = scale
            val bitmap =
                BitmapFactory.decodeStream(
                    context.contentResolver?.openInputStream(uri),
                    null,
                    secondOptions
                )
            bitmap?.let {
                val exif = ExifInterface(getPath(context, uri))
                exif.getAttribute(ExifInterface.TAG_ORIENTATION)?.let {
                    if (!TextUtils.isEmpty(it)) {
                        when (it.toInt()) {
                            ExifInterface.ORIENTATION_NORMAL -> {
                                return getRotatedImage(bitmap, 0f)
                            }
                            ExifInterface.ORIENTATION_ROTATE_90 -> {
                                return getRotatedImage(bitmap, 90f)
                            }
                            ExifInterface.ORIENTATION_ROTATE_180 -> {
                                return getRotatedImage(bitmap, 180f)
                            }
                            ExifInterface.ORIENTATION_ROTATE_270 -> {
                                return getRotatedImage(bitmap, 270f)
                            }
                        }
                    }
                }
            }
            return bitmap
        }

        fun createImageFile(context: Context, bitmap: Bitmap): File {
            val file = File(context.externalCacheDir, "tmpImageFile")
            var fos: FileOutputStream? = null
            try {
                file.createNewFile()
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } catch (e: IOException) {
            } finally {
                try {
                    fos?.let {
                        it.flush()
                        it.close()
                    }
                } catch (e: IOException) {
                }
            }
            return file
        }

        private fun getPath(context: Context, uri: Uri): String {
            var cursor: Cursor? = null
            return try {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val loader = CursorLoader(context, uri, projection, null, null, null)
                cursor = loader.loadInBackground()
                val index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ?: 0
                cursor?.moveToFirst()
                cursor?.getString(index).toString()
            } finally {
                cursor?.close()
            }
        }

        private fun getRotatedImage(bitmap: Bitmap, rotate: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(rotate)
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}