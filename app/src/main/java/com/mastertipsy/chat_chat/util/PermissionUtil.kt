package com.mastertipsy.chat_chat.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtil(private val context: Context) {
    private val permissionGranted = PackageManager.PERMISSION_GRANTED
    private val permissionCamera = Manifest.permission.CAMERA
    private val permissionReadStorage = Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissionWriteStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun isAllNeededPermissionsGranted(): Boolean =
        isPermissionGranted(permissionCamera)
                && isPermissionGranted(permissionReadStorage)
                && isPermissionGranted(permissionWriteStorage)

    fun requestAllNeededPermissions() {
        val activity = context as Activity
        val permissions = arrayOf(permissionCamera, permissionReadStorage, permissionWriteStorage)
        ActivityCompat.requestPermissions(activity, permissions, 7888)
    }

    private fun isPermissionGranted(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == permissionGranted
}