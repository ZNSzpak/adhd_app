package com.example.projekt_inz.ui.plan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.projekt_inz.MainActivity
import com.example.projekt_inz.R

class ShowBlockDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_ID = "arg_id"
        private const val ARG_NAME = "arg_name"
        private const val ARG_DAY = "arg_day"
        private const val ARG_START = "arg_start"
        private const val ARG_END = "arg_end"

        fun newInstance(
            id: Int,
            name: String,
            dayOfWeek: Int,
            startMinute: Int,
            endMinute: Int,
            onDelete: ((Int) -> Unit)? = null
        ): ShowBlockDialogFragment {

            val fragment = ShowBlockDialogFragment()

            val args = Bundle().apply {
                putInt(ARG_ID, id)
                putString(ARG_NAME, name)
                putInt(ARG_DAY, dayOfWeek)
                putInt(ARG_START, startMinute)
                putInt(ARG_END, endMinute)
            }
            fragment.arguments = args
            fragment.onDeleteBlock = onDelete
            return fragment
        }
    }

    private var onDeleteBlock: ((Int) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.show_block_dialog, null)

        // extract block fields
        val id = requireArguments().getInt(ARG_ID)
        val name = requireArguments().getString(ARG_NAME)!!
        val day = requireArguments().getInt(ARG_DAY)
        val start = requireArguments().getInt(ARG_START)
        val end = requireArguments().getInt(ARG_END)

        val block = PlanEntity(
            id = id,
            name = name,
            dayOfWeek = day,
            startMinute = start,
            endMinute = end,
        )

        // find views
        val titleView = view.findViewById<TextView>(R.id.blockTitleShow)
        val timeView = view.findViewById<TextView>(R.id.blockTimeShow)
        val closeBtn = view.findViewById<ImageView>(R.id.closeBlock)
        val editBtn = view.findViewById<ImageView>(R.id.editBlock)
        val deleteBtn = view.findViewById<ImageView>(R.id.deleteBlock)

        // fill UI
        titleView.text = block.name
        timeView.text = block.formatTimeRange()

        closeBtn.setOnClickListener { dismiss() }

        deleteBtn.setOnClickListener {
            onDeleteBlock?.invoke(block.id) // notify caller
            dismiss()
        }

//        editBtn.setOnClickListener {
//            EditBlockDialogFragment(block) { updatedBlock ->
//                (requireActivity() as MainActivity).planViewModel.updateBlock(updatedBlock)
//            }.show(parentFragmentManager, "EditBlockDialog")
//            dismiss()
//        }


        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}