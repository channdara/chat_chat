package com.mastertipsy.chat_chat.ui.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.model.AppConst
import com.mastertipsy.chat_chat.model.User
import com.mastertipsy.chat_chat.presentor.repository.RegisterRepository
import com.mastertipsy.chat_chat.presentor.view.RegisterView
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.util.MediaUtil
import java.io.File

class RegisterActivity : AppCompatActivity(), RegisterView {
    companion object {
        fun start(context: Context) =
            context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    private lateinit var repo: RegisterRepository

    private val layoutBottomSheet by lazy { findViewById<LinearLayout>(R.id.layout_bottom_sheet_media_picker) }
    private val layoutCameraPicker by lazy { findViewById<LinearLayout>(R.id.layout_media_picker_camera) }
    private val layoutGalleryPicker by lazy { findViewById<LinearLayout>(R.id.layout_media_picker_gallery) }
    private val bottomSheet by lazy { BottomSheetBehavior.from(layoutBottomSheet) }
    private val ivUserProfile by lazy { findViewById<AppCompatImageView>(R.id.iv_user_profile) }
    private val btnBack by lazy { findViewById<AppCompatImageButton>(R.id.btn_back) }
    private val btnBottomSheetClear by lazy { findViewById<AppCompatButton>(R.id.btn_bottom_sheet_clear) }
    private val btnRegister by lazy { findViewById<AppCompatButton>(R.id.btn_register_send) }
    private val etUsername by lazy { findViewById<AppCompatEditText>(R.id.et_register_username) }
    private val etPassword by lazy { findViewById<AppCompatEditText>(R.id.et_register_password) }
    private val etConfirmPassword by lazy { findViewById<AppCompatEditText>(R.id.et_register_confirm_password) }
    private val etEmailAddress by lazy { findViewById<AppCompatEditText>(R.id.et_register_email) }
    private val etPhoneNumber by lazy { findViewById<AppCompatEditText>(R.id.et_register_phone_number) }

    private var imageUri: Uri? = null
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        repo = RegisterRepository(this, this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val uri = if (requestCode == AppConst.GALLERY_CODE) {
            data?.data
        } else {
            imageUri
        }
        uri?.let {
            MediaUtil.compressImage(this, it)?.let { bitmap ->
                ivUserProfile.setImageBitmap(bitmap)
                imageFile = MediaUtil.createImageFile(this, bitmap)
            }
        }
        bottomSheet.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onRegisterSuccess() {
        AlertUtil.showAlertDialog(
            this,
            getString(R.string.success),
            getString(R.string.register_success),
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
                ChatRoomListActivity.startNewTaskClearTask(this)
            }
        )
    }

    override fun onError(message: String) {
        AlertUtil.showAlertDialog(this, getString(R.string.error), message)
    }

    private fun setupListener() {
        btnBack.setOnClickListener { onBackPressed() }
        ivUserProfile.setOnClickListener {
            if (bottomSheet.state == BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
        }
        layoutCameraPicker.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            imageUri = this.contentResolver.insert(externalContentUri, contentValues)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, AppConst.CAMERA_CODE)
        }
        layoutGalleryPicker.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, AppConst.GALLERY_CODE)
        }
        btnBottomSheetClear.setOnClickListener {
            ivUserProfile.setImageDrawable(getDrawable(R.drawable.ic_user_hint))
            imageUri = null
        }
        btnRegister.setOnClickListener {
            if (!isAllDataValidated()) return@setOnClickListener
            val user = User(
                "",
                etUsername.text.toString(),
                etConfirmPassword.text.toString(),
                etEmailAddress.text.toString(),
                etPhoneNumber.text.toString()
            )
            val uri = Uri.fromFile(imageFile)
            repo.registerUser(user, uri)
        }
    }

    private fun isAllDataValidated(): Boolean {
        if (etUsername.text.isNullOrEmpty()) {
            etUsername.error = getString(R.string.error_required_username)
            etUsername.requestFocus()
            return false
        }
        if (etUsername.text.toString().length > 64) {
            etUsername.error = getString(R.string.error_username_too_long)
            etUsername.requestFocus()
            return false
        }
        if (etPassword.text.isNullOrEmpty()) {
            etPassword.error = getString(R.string.error_required_password)
            etPassword.requestFocus()
            return false
        }
        if (etPassword.text.toString().length < AppConst.MAX_PASSWORD) {
            etPassword.error = getString(R.string.error_password_greater_than_6)
            etPassword.requestFocus()
            return false
        }
        if (etConfirmPassword.text.isNullOrEmpty()) {
            etConfirmPassword.error = getString(R.string.error_required_confirm_password)
            etConfirmPassword.requestFocus()
            return false
        }
        if (etEmailAddress.text.isNullOrEmpty()) {
            etEmailAddress.error = getString(R.string.error_required_email)
            etEmailAddress.requestFocus()
            return false
        }
        if (etPhoneNumber.text.isNullOrEmpty()) {
            etPhoneNumber.error = getString(R.string.error_required_phone_number)
            etPhoneNumber.requestFocus()
            return false
        }
        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            etConfirmPassword.error = getString(R.string.error_required_confirm_password_match)
            etConfirmPassword.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.text.toString()).matches()) {
            etEmailAddress.error = getString(R.string.error_email_not_correct)
            etEmailAddress.requestFocus()
            return false
        }
        return true
    }
}
