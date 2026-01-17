package com.example.projekt_inz.ui.routines.routine_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.projekt_inz.R
import com.example.projekt_inz.ui.todolist.AddTaskDialogFragment
import com.example.projekt_inz.ui.todolist.EditTaskDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.concurrent.TimeUnit

class TodoListFragment : Fragment() {

    private var listId: Long = 0
    private lateinit var listName: String
    private lateinit var titleTextView: TextView
    private lateinit var taskListR: RecyclerView
    private lateinit var addButton: FloatingActionButton
    private lateinit var viewModel: TaskRoutinesViewModel
    private lateinit var taskAdapter: TaskRoutinesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listId = arguments?.getLong("listId") ?: 0L
        listName = arguments?.getString("listName") ?: "Unnamed"
        if (listId == 0L) {
            throw IllegalStateException("Invalid listId passed to TodoListFragment")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextView = view.findViewById(R.id.todoListTitle)
        titleTextView.text = listName

        taskListR = view.findViewById(R.id.taskListR)
        addButton = view.findViewById(R.id.addButton)

        val db = RoutineTaskDatabase.getDatabase(requireContext())
        val taskDao = db.taskDao()
        val repo = TaskRoutinesRepository(taskDao)
        val factory = TaskRoutinesViewMFactory(repo, listId)
        viewModel = ViewModelProvider(this, factory)[TaskRoutinesViewModel::class.java]

        setupRecyclerView()
        observeViewModel()
        setupAddButton()

        val callback = TaskDragCallback(taskAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(taskListR)

        scheduleMidnightReset()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskRoutinesAdapter(
            onEdit = { task ->
                EditTaskDialogFragment(task.text) { newText ->
                    viewModel.updateTask(task, newText)
                }.show(parentFragmentManager, "EditTask")
            },
            onDelete = { task -> viewModel.deleteTask(task) },
            onChecked = { task, checked -> viewModel.toggleTask(task, checked) },
            onMove = { fromPosition, toPosition ->
                viewModel.moveTask(fromPosition, toPosition)
            }
        )

        taskListR.layoutManager = LinearLayoutManager(requireContext())
        taskListR.adapter = taskAdapter
    }

    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasksFromDb ->
            val currentList = taskAdapter.currentList.toMutableList()

            val taskMap = tasksFromDb.associateBy { it.id }

            val updatedList = currentList.mapNotNull { oldTask ->
                taskMap[oldTask.id]
            }.toMutableList()

            val newTasks = tasksFromDb.filter { task ->
                updatedList.none { it.id == task.id }
            }.sortedBy { it.position }
            newTasks.forEach { newTask ->

                val insertIndex = updatedList.indexOfFirst { it.position > newTask.position }
                if (insertIndex == -1) updatedList.add(newTask)
                else updatedList.add(insertIndex, newTask)
            }

            taskAdapter.submitList(updatedList)
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTaskRFragment(listId) { newTask ->

                    viewModel.addTask(newTask)

            }.show(parentFragmentManager, "AddTaskDialog")
        }
    }

    private fun scheduleMidnightReset() {
        val now = Calendar.getInstance()
        val nextMidnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = nextMidnight.timeInMillis - now.timeInMillis

        val resetWork = PeriodicWorkRequestBuilder<ResetRoutinesWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "reset_routines",
            ExistingPeriodicWorkPolicy.KEEP,
            resetWork
        )
    }
}