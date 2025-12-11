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

class AddTimeBlockDialogFragment(
    private val onBlockAdded: (PlanEntity) -> Unit
) : DialogFragment() {

    private lateinit var editName: EditText
    private lateinit var spinnerDay: Spinner
    private lateinit var startPicker: TimePicker
    private lateinit var endPicker: TimePicker
    private lateinit var addButton: Button
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
    ): View {
        return inflater.inflate(R.layout.dialog_add_time_block, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find views
        editName = view.findViewById(R.id.editBlockName)
        spinnerDay = view.findViewById(R.id.spinnerDay)
        startPicker = view.findViewById(R.id.startTimePicker)
        endPicker = view.findViewById(R.id.endTimePicker)
        addButton = view.findViewById(R.id.btnAddBlock)
        closeButton = view.findViewById(R.id.closeButtonAdd)

        startPicker.setIs24HourView(true)
        endPicker.setIs24HourView(true)
        // Setup day spinner
        val days = listOf("Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek")
        spinnerDay.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, days).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Close button dismisses dialog
        closeButton.setOnClickListener { dismiss() }

        // Add button click listener
        addButton.setOnClickListener {
            val name = editName.text.toString().trim()
            if (name.isEmpty()) {
                editName.error = "Enter block name"
                return@setOnClickListener
            }

            val dayIndex = spinnerDay.selectedItemPosition  // 0 = Mon, 4 = Fri
            val startMinutes = startPicker.hour * 60 + startPicker.minute
            val endMinutes = endPicker.hour * 60 + endPicker.minute

            if (endMinutes <= startMinutes) {
                Toast.makeText(requireContext(), "End time must be after start time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newBlock = PlanEntity(
                name = name,
                dayOfWeek = dayIndex, // 0..4
                startMinute = startMinutes,
                endMinute = endMinutes
            )

            onBlockAdded(newBlock) // <-- This was missing

            dismiss()
        }
    }
}
