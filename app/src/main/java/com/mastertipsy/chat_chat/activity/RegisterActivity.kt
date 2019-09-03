package com.mastertipsy.chat_chat.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.util.PermissionUtil

class RegisterActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    private val layoutBottomSheet by lazy { findViewById<LinearLayout>(R.id.layout_bottom_sheet_media_picker) }

    private val bottomSheet by lazy { BottomSheetBehavior.from(layoutBottomSheet) }

    private val btnBack by lazy { findViewById<AppCompatImageButton>(R.id.btn_back) }
    private val btnBottomSheetClear by lazy { findViewById<AppCompatButton>(R.id.btn_bottom_sheet_clear) }
    private val ivUserProfile by lazy { findViewById<AppCompatImageView>(R.id.iv_user_profile) }
    private val layoutCameraPicker by lazy { findViewById<LinearLayout>(R.id.layout_media_picker_camera) }
    private val layoutGalleryPicker by lazy { findViewById<LinearLayout>(R.id.layout_media_picker_gallery) }

    private val camera = 1
    private val gallery = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupListener()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (bottomSheet.state == BottomSheetBehavior.STATE_EXPANDED) {
                val outRect = Rect()
                layoutBottomSheet.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt()))
                    bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        data?.let {
            if (requestCode == gallery) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it.data)
                ivUserProfile.setImageBitmap(bitmap)
            }
            if (requestCode == camera) {
                val bitmap = it.extras?.get("data") as Bitmap
                ivUserProfile.setImageBitmap(bitmap)

            }
        }
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupListener() {
        btnBack.setOnClickListener { onBackPressed() }
        ivUserProfile.setOnClickListener {
            if (!PermissionUtil.isCameraGranted(this)) {
                PermissionUtil.requestPermissions(this)
            }
            if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        layoutCameraPicker.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, camera)
        }
        layoutGalleryPicker.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, gallery)
        }
        btnBottomSheetClear.setOnClickListener {
            ivUserProfile.setImageDrawable(getDrawable(R.drawable.ic_user_hint))
        }
    }
}
