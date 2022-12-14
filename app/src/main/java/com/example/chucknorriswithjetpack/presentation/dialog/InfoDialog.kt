package com.example.chucknorriswithjetpack.presentation.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDialog : DialogFragment() {

    private var yesListener: (() -> Unit)? = null

    fun setYesListener(listener: () -> Unit) {
        yesListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(arguments?.getString("title"))
            .setMessage(arguments?.getString("msg"))
            .setPositiveButton("OK") { _, _ ->
                yesListener?.let { yes ->
                    yes()
                }
            }
            .create()
    }

    companion object {
        const val TAG = "InfoDialog"
    }
}