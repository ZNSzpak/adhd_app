package com.example.projekt_inz.ui.notes

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R

class ShowNoteDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_NOTE_TEXT = "note_text"

        fun newInstance(text: String): ShowNoteDialogFragment {
            val fragment = ShowNoteDialogFragment()
            val args = Bundle()
            args.putString(ARG_NOTE_TEXT, text)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.show_note_dialog, null)

        val fullNoteText = view.findViewById<TextView>(R.id.fullNoteText)
        val closeBtn = view.findViewById<ImageView>(R.id.closeNote)

        fullNoteText.text = arguments?.getString(ARG_NOTE_TEXT) ?: ""

        closeBtn.setOnClickListener {
            dismiss()
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(view)

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.show_note_dialog, container, false)
        val fullText = view.findViewById<TextView>(R.id.fullNoteText)

        fullText.text = arguments?.getString(ARG_NOTE_TEXT)

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fullText = view.findViewById<TextView>(R.id.fullNoteText)
        val closeButton = view.findViewById<ImageView>(R.id.closeNote)

        fullText.text = arguments?.getString(ARG_NOTE_TEXT) ?: ""

        closeButton.setOnClickListener {
            dismiss()
        }
    }
}