package com.example.projekt_inz.ui.plan

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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

    lateinit var viewModel: PlanViewModel
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

        table.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    table.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    // Row index 1 = row for 07:00 (row 0 = header)
                    val row07 = table.getChildAt(1) as TableRow

                    row07.measure(
                        View.MeasureSpec.UNSPECIFIED,
                        View.MeasureSpec.UNSPECIFIED
                    )

                    val hourHeight = row07.measuredHeight
                    viewModel.hourHeight = hourHeight
                }
            }
        )


        observeViewModel()
        setupAddButton()
    }

    private fun setupTable() {

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
//        ShowBlockDialogFragment.newInstance(
//            id = block.id,
//            name = block.name,
//            dayOfWeek = block.dayOfWeek,
//            startMinute = block.startMinute,
//            endMinute = block.endMinute
//        ) { idToDelete ->
//            viewModel.deleteBlockById(idToDelete)
//        }.show(childFragmentManager, "ShowBlockDialog")
//    }

    private val onBlockClick: (PlanEntity) -> Unit = { block ->
        ShowBlockDialogFragment.newInstance(
            id = block.id,
            name = block.name,
            dayOfWeek = block.dayOfWeek,
            startMinute = block.startMinute,
            endMinute = block.endMinute,
            onEdit = { updatedBlock ->
                EditBlockDialogFragment(updatedBlock) { updated ->
                    viewModel.updateBlock(updated) // <-- this updates DB and triggers UI refresh
                }.show(childFragmentManager, "EditBlockDialog")// use ViewModel directly
            },
            onDelete = { idToDelete ->
                viewModel.deleteBlockById(idToDelete)
            }
        ).show(childFragmentManager, "ShowBlockDialog")
    }

    private fun drawBlocks(blocks: List<PlanEntity>) {
        val container = requireView().findViewById<FrameLayout>(R.id.blockContainer)
        val table = requireView().findViewById<TableLayout>(R.id.timetableTable)

        table.post {
            container.removeAllViews()

            val hourHeightPx = viewModel.hourHeight
            val firstHour = 7

            val row07 = table.getChildAt(1) as TableRow
            val yStart07 = row07.top


            val offsetY = table.y - container.y

            blocks.forEach { block ->

                val startOffsetMinutes = block.startMinute - firstHour * 60
                val durationMinutes = block.endMinute - block.startMinute

                val topPx =
                    offsetY +
                            yStart07 +
                            ((startOffsetMinutes / 60f) * hourHeightPx).toInt()

                val heightPx =
                    ((durationMinutes / 60f) * hourHeightPx).toInt()

                val dayIndex = block.dayOfWeek
                val columnWidth = table.width / 6
                val left = columnWidth * (dayIndex + 1)

                val blockView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.plan_cell, container, false)

                blockView.findViewById<TextView>(R.id.blockTitle).text = block.name
                blockView.findViewById<TextView>(R.id.blockTime).text = block.formatTimeRange()

                val params = FrameLayout.LayoutParams(columnWidth, heightPx)
                params.leftMargin = left
                params.topMargin = topPx.toInt()
                blockView.layoutParams = params

                blockView.setOnClickListener {
                    onBlockClick(block)
                }

                container.addView(blockView)
            }
        }
    }

    private fun dpToPx(dp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
}

