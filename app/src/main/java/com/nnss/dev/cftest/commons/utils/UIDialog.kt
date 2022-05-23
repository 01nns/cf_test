package com.nnss.dev.cftest.commons.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import com.nnss.dev.cftest.R
import com.nnss.dev.cftest.databinding.DialogLoaderBinding

object UIDialog {
    fun loader(context: Context?): Dialog? {
        val dialog = context?.let { Dialog(it, R.style.DialogTheme) }
        val binding: DialogLoaderBinding = DialogLoaderBinding.inflate(LayoutInflater.from(context))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(binding.root)
        dialog?.setCancelable(false)
        dialog?.window?.apply {
            decorView.setBackgroundResource(android.R.color.transparent)
            setDimAmount(0f)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
        return dialog
    }

}