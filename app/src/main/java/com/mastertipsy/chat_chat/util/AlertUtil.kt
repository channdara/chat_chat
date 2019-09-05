package com.mastertipsy.chat_chat.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.mastertipsy.chat_chat.R

class AlertUtil {
    companion object {
        fun showAlertDialog(context: Context, title: String, message: String?) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message ?: context.getString(R.string.error_unknown_error))
                .setNegativeButton(context.getString(R.string.ok), null)
                .show()
        }

        fun showAlertDialog(
            context: Context,
            title: String,
            message: String?,
            listener: DialogInterface.OnClickListener
        ) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message ?: context.getString(R.string.error_unknown_error))
                .setNegativeButton(context.getString(R.string.ok), listener)
                .show()
        }

        fun progressDialog(context: Context): Dialog = Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.custom_progress_bar)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        }
    }
}