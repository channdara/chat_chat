package com.mastertipsy.chat_chat.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.mastertipsy.chat_chat.R
import com.mastertipsy.chat_chat.model.AppConst
import com.mastertipsy.chat_chat.presentor.repository.LoginRepository
import com.mastertipsy.chat_chat.util.AlertUtil
import com.mastertipsy.chat_chat.presentor.view.LoginView

class LoginActivity : AppCompatActivity(), LoginView {
    companion object {
        fun startNewTaskClearTask(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
            (context as Activity).finish()
        }
    }

    private lateinit var repo: LoginRepository

    private val btnGotoRegister by lazy { findViewById<AppCompatButton>(R.id.btn_login_register) }
    private val btnLogin by lazy { findViewById<AppCompatButton>(R.id.btn_login) }
    private val etEmail by lazy { findViewById<AppCompatEditText>(R.id.et_login_email) }
    private val etPassword by lazy { findViewById<AppCompatEditText>(R.id.et_login_password) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        repo = LoginRepository(this, this)
        setupListener()
    }

    override fun onLoginSuccess() {
        ChatRoomListActivity.startNewTaskClearTask(this)
    }

    override fun onError(message: String) {
        AlertUtil.showAlertDialog(this, getString(R.string.error), message)
    }

    private fun setupListener() {
        btnGotoRegister.setOnClickListener { RegisterActivity.start(this) }
        btnLogin.setOnClickListener {
            if (!isAllDataValidated()) return@setOnClickListener
            repo.login(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    private fun isAllDataValidated(): Boolean {
        if (etEmail.text.isNullOrEmpty()) {
            etEmail.error = getString(R.string.error_required_email)
            etEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            etEmail.error = getString(R.string.error_email_not_correct)
            etEmail.requestFocus()
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
        return true
    }
}
