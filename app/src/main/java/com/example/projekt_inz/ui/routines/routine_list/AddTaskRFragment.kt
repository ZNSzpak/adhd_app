package com.example.projekt_inz.ui.routines.routine_list

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddTaskRFragment (
    private val listId: Long,                       // ID of the routine/list
    private val onTaskAdded: (String) -> Unit
) : DialogFragment() {

    private lateinit var inputEt: EditText
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
        return inflater.inflate(R.layout.fragment_add_todo_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inputEt = view.findViewById(R.id.todoEt)
        addButton = view.findViewById(R.id.addButtonPopup)
        closeButton = view.findViewById(R.id.todoClose)

        closeButton.setOnClickListener { dismiss() }

        addButton.setOnClickListener {
            val text = inputEt.text.toString().trim()
            if (text.isNotEmpty()) {
               // val newTask = TaskEntityRoutines(listId = listId, text = text)
                onTaskAdded(text)
                dismiss()
            } else {
                inputEt.error = "Enter a task"
            }
        }
    }
}