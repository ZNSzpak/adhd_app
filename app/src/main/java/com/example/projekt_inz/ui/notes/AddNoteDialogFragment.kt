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

class AddNoteDialogFragment(
    private val onNoteAdded: (NoteEntity) -> Unit
) : DialogFragment(){

    private lateinit var textNote: EditText
    private lateinit var addButton: FloatingActionButton
    private lateinit var closeButton: ImageView

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
        textNote = view.findViewById(R.id.noteText)
        addButton = view.findViewById(R.id.addNotePopup)
        closeButton = view.findViewById(R.id.addNClose)

        closeButton.setOnClickListener { dismiss() }

        addButton.setOnClickListener {
            val text = textNote.text.toString().trim()
            if (text.isNotEmpty()) {
                val newNote = NoteEntity(id=id, text = text)
                onNoteAdded(newNote)
                dismiss()
            } else {
                textNote.error = "Zapisz blok"
            }
        }
    }
}