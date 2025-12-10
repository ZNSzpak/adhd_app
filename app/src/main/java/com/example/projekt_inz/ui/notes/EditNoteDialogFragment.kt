package com.example.projekt_inz.ui.notes

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditNoteDialogFragment (
    private val currentText: String,
    private val onNoteEdited: (String) -> Unit
) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val closeBtn: ImageView = view.findViewById(R.id.addNClose)
        val addBtn: FloatingActionButton = view.findViewById(R.id.addNotePopup)
        val inputEt: EditText = view.findViewById(R.id.noteText)

        closeBtn.setOnClickListener { dismiss() }

        inputEt.setText(currentText)
        inputEt.setSelection(currentText.length)

        addBtn.setOnClickListener {
            val newName = inputEt.text.toString().trim()

            if (newName.isNotEmpty()) {
                onNoteEdited(newName)
                dismiss()
            } else {
                inputEt.error = "Wpisz nazwę"
            }
        }
    }
}