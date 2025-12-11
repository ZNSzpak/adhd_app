package com.example.projekt_inz.ui.plan

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.notes.AddNoteDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PlanFragment : Fragment() {

    private lateinit var viewModel: PlanViewModel
    private lateinit var addButton: FloatingActionButton
    private lateinit var table: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = PlanDatabase.getInstance(requireContext()).planDao()
        val repository = PlanRepository(dao)
        val factory = PlanViewModelFactory(repository)

        findViews(view)

        viewModel = ViewModelProvider(this, factory)[PlanViewModel::class.java]

        setupTable()
        observeViewModel()
        setupAddButton()
    }

    private fun setupTable() {
        // Optional: dynamically generate rows 07:00–20:00 if you don't want to hardcode in XML

        for (hour in 7..20) {
            val row = TableRow(requireContext())
            val timeText = TextView(requireContext())
            timeText.text = "%02d:00".format(hour)
            timeText.width = dpToPx(50f)
            timeText.gravity = Gravity.CENTER
            row.addView(timeText)

            for (day in 1..5) { // Mon–Fri
                val cell = FrameLayout(requireContext())
                cell.id = View.generateViewId()
                cell.layoutParams = TableRow.LayoutParams(
                    0,
                    dpToPx(60f),
                    1f
                )
                row.addView(cell)
            }

            table.addView(row)
        }
    }


    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.blocks.collect { blocks ->
                    Log.d("PLAN_DEBUG", "Blocks emitted: $blocks")
                    drawBlocks(blocks)
                }
            }
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTimeBlockDialogFragment { newBlock ->
                viewModel.addBlock(newBlock)
            }.show(parentFragmentManager, "AddBlockDialog")
        }
    }

    private fun findViews(view: View) {
        addButton = view.findViewById(R.id.addBlockButton)
        table = requireView().findViewById(R.id.timetableTable)
    }

//    private val onBlockClick: (PlanEntity) -> Unit = { block ->
//        ShowBlockDialogFragment(block,
//            onEdit = onBlockEdit,
//            onDelete = onBlockDelete
//        ).show(parentFragmentManager, "ShowBlockDialog")
//    }

    private val onBlockEdit: (PlanEntity) -> Unit = { block ->
        EditBlockDialogFragment(block) { updated ->
            viewModel.updateBlock(updated)
        }.show(parentFragmentManager, "EditBlockDialog")
    }

    private val onBlockDelete: (PlanEntity) -> Unit = { block ->
        viewModel.deleteBlock(block)
    }

    private fun drawBlocks(blocks: List<PlanEntity>) {
        val container = requireView().findViewById<FrameLayout>(R.id.blockContainer)
        val table = requireView().findViewById<TableLayout>(R.id.timetableTable)

        table.post {
            // Clear old blocks
            container.removeAllViews()

            val firstRow = table.getChildAt(1) as TableRow
            val hourHeightPx = firstRow.height
            val firstHour = 7

            blocks.forEach { block ->
                val startOffsetMinutes = block.startMinute - firstHour * 60
                val durationMinutes = block.endMinute - block.startMinute

                val topPx = (startOffsetMinutes * hourHeightPx / 60f).toInt()
                val heightPx = (durationMinutes * hourHeightPx / 60f).toInt()

                val dayColumnIndex = block.dayOfWeek // 0=Mon
                val columnWidth = table.width / 6 // 6 columns including time
                val left = columnWidth * (dayColumnIndex + 1) // +1 because first column is time

                val blockView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.plan_cell, container, false)

                blockView.findViewById<TextView>(R.id.blockTitle).text = block.name
                blockView.findViewById<TextView>(R.id.blockTime).text = block.formatTimeRange()

                val params = FrameLayout.LayoutParams(columnWidth, heightPx)
                params.leftMargin = left
                params.topMargin = topPx
                blockView.layoutParams = params

                // --- CLICK TO OPEN SHOW DIALOG ---
                blockView.setOnClickListener {

//                    val dialog = ShowBlockDialogFragment.newInstance(block)
//
//                    dialog.onEditClick = { updated ->
//                        viewModel.updateBlock(updated)
//                    }
//
//                    dialog.onDeleteClick = {
//                        viewModel.deleteBlock(block)
//                    }

                   // dialog.show(childFragmentManager, "ShowBlockDialog")
                }

                container.addView(blockView)
            }
        }
    }
//    private fun placeBlock(block: PlanEntity) {
//        val table = requireView().findViewById<TableLayout>(R.id.timetableTable)
//        val container = requireView().findViewById<FrameLayout>(R.id.blockContainer)
//        val firstRow = table.getChildAt(1) as TableRow
//        val hourHeightPx = firstRow.height
//        val firstHour = 7
//        val startOffsetMinutes = block.startMinute - firstHour * 60
//        val durationMinutes = block.endMinute - block.startMinute
//
//        val topPx = (startOffsetMinutes * hourHeightPx / 60f).toInt()
//        val heightPx = (durationMinutes * hourHeightPx / 60f).toInt()
//
//
//        val dayColumnIndex = block.dayOfWeek // 0=Mon
//
//        // Total minutes from 07:00
//
//        val endOffsetMinutes = block.endMinute - firstHour * 60
//
//
//        // Calculate left and width based on table
//        val columnWidth = table.width / 6 // 6 columns including time
//        val left = columnWidth * (dayColumnIndex + 1) // +1 because first column is time
//
//        val blockView = LayoutInflater.from(requireContext())
//            .inflate(R.layout.plan_cell, container, false)
//
//        val title = blockView.findViewById<TextView>(R.id.blockTitle)
//        val time = blockView.findViewById<TextView>(R.id.blockTime)
//
//        title.text = block.name
//        time.text = block.formatTimeRange()
//
//        val params = FrameLayout.LayoutParams(columnWidth, heightPx)
//        params.leftMargin = left
//        params.topMargin = topPx
//
//        blockView.layoutParams = params
//        container.addView(blockView)
//    }


    private fun dpToPx(dp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
}

