package com.mastertipsy.chat_chat.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil {
    companion object {
        private const val permissionGranted = PackageManager.PERMISSION_GRANTED
        private const val permissionCamera = Manifest.permission.CAMERA

        fun isCameraGranted(context: Context): Boolean =
            ContextCompat.checkSelfPermission(context, permissionCamera) == permissionGranted

        fun requestPermissions(activity: Activity) =
            ActivityCompat.requestPermissions(activity, arrayOf(permissionCamera), 100)
    }
}