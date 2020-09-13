package com.mastertipsy.chat_chat.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.util.PermissionUtil

class SplashActivity : AppCompatActivity() {
    private lateinit var permissionUtil: PermissionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        permissionUtil = PermissionUtil(this)
        checkAllPermissions()
    }

    private fun checkAllPermissions() {
        if (permissionUtil.isAllNeededPermissionsGranted()) {
            checkLoggedInUser()
            return
        }
        permissionUtil.requestAllNeededPermissions()
        checkAllPermissions()
    }

    private fun checkLoggedInUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            LoginActivity.start(this)
            return
        }
        ChatRoomListActivity.start(this, user.email ?: "")
    }
}
