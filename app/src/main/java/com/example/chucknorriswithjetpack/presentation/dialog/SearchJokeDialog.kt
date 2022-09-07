package com.example.chucknorriswithjetpack.presentation.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.chucknorriswithjetpack.databinding.DialogSearchDialogueBinding
import timber.log.Timber

class SearchJokeDialog : DialogFragment() {

    private lateinit var binding: DialogSearchDialogueBinding

    private var yesListener: ((text: String) -> Unit)? = null

    fun setSearchListener(listener: (text: String) -> Unit) {
        yesListener = listener
    }

    companion object {
        const val TAG = "SearchJokeDialog"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
    ): View {
        binding = DialogSearchDialogueBinding.inflate(layoutInflater)
        binding.searchBtn.setOnClickListener {
            yesListener?.let { yes ->
                yes(binding.inputEdtTxt.text.toString())
                dismiss()
            } ?: Timber.i("Null yes listener")
        }
        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        Timber.i("OnCreate View Search Dialog")

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

}