package com.example.projekt_inz.ui.plan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.R

class EditBlockDialogFragment(
    private val block: PlanEntity,
    private val onBlockEdited: (PlanEntity) -> Unit
) : DialogFragment() {

    private lateinit var editName: EditText
    private lateinit var spinnerDay: Spinner
    private lateinit var startPicker: TimePicker
    private lateinit var endPicker: TimePicker
    private lateinit var saveButton: Button
    private lateinit var closeButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NO_TITLE,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
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
    ): View {
        return inflater.inflate(R.layout.dialog_add_time_block, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        editName = view.findViewById(R.id.editBlockName)
        spinnerDay = view.findViewById(R.id.spinnerDay)
        startPicker = view.findViewById(R.id.startTimePicker)
        endPicker = view.findViewById(R.id.endTimePicker)
        saveButton = view.findViewById(R.id.btnAddBlock)   // reused button
        closeButton = view.findViewById(R.id.closeButtonAdd)

        startPicker.setIs24HourView(true)
        endPicker.setIs24HourView(true)

        // Fill spinner
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
        spinnerDay.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days)
            .apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        // Populate existing values
        editName.setText(block.name)
        spinnerDay.setSelection(block.dayOfWeek) // 0–4 already
        startPicker.hour = block.startMinute / 60
        startPicker.minute = block.startMinute % 60
        endPicker.hour = block.endMinute / 60
        endPicker.minute = block.endMinute % 60

        saveButton.text = "Save"

        closeButton.setOnClickListener { dismiss() }

        saveButton.setOnClickListener {
            val newName = editName.text.toString().trim()
            if (newName.isEmpty()) {
                editName.error = "Enter block name"
                return@setOnClickListener
            }

            val newDay = spinnerDay.selectedItemPosition
            val newStart = startPicker.hour * 60 + startPicker.minute
            val newEnd = endPicker.hour * 60 + endPicker.minute

            if (newEnd <= newStart) {
                Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create updated block
            val updated = block.copy(
                name = newName,
                dayOfWeek = newDay,
                startMinute = newStart,
                endMinute = newEnd
            )

            onBlockEdited(updated)
            dismiss()
        }
    }
}