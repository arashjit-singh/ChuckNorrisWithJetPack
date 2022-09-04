package com.example.chucknorriswithjetpack.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.chucknorriswithjetpack.databinding.DialogSearchDialogueBinding

class SearchJokeDialog : DialogFragment() {

    lateinit var binding: DialogSearchDialogueBinding

    private var yesListener: ((text: String) -> Unit)? = null

    fun setSearchListener(listener: (text: String) -> Unit) {
        yesListener = listener
    }

    companion object {
        const val TAG = "SearchJokeDialog"
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSearchDialogueBinding.inflate(layoutInflater)
        binding.searchBtn.setOnClickListener {
            yesListener?.let { yes ->
                yes(binding.inputEdtTxt.text.toString())
                dismiss()
            }
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}