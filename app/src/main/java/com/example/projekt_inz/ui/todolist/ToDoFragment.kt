package com.example.projekt_inz.ui.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoFragment : Fragment() {

    private lateinit var taskList: RecyclerView
    private lateinit var addButton: FloatingActionButton

    private lateinit var taskAdapter: TaskAdapter
    private val taskListData = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        setupRecyclerView()
        setupAddButton()
    }

    private fun findViews(view: View) {
        taskList = view.findViewById(R.id.taskList)
        addButton = view.findViewById(R.id.addButton)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            taskListData,
            onEdit = { task, pos -> editTask(task, pos) },
            onDelete = { task, pos -> deleteTask(pos) },
            onChecked = { task, pos, checked -> toggleTask(task, pos, checked) }
        )

        taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList.adapter = taskAdapter
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTaskDialogFragment { newTaskText ->
                val task = Task(newTaskText, false)
                taskAdapter.addTask(task)
            }.show(parentFragmentManager, "AddTaskDialog")
        }
    }

    private fun editTask(task: Task, position: Int) {
        EditTaskDialogFragment(task.text) { newText ->
            val updatedTask = task.copy(text = newText)
            taskAdapter.updateTask(position, updatedTask)
        }.show(parentFragmentManager, "EditTaskDialog")
    }

    private fun deleteTask(position: Int) {
        taskAdapter.removeTask(position)
    }

    private fun toggleTask(task: Task, position: Int, checked: Boolean) {
        val updatedTask = task.copy(isDone = checked)
        taskAdapter.updateTask(position, updatedTask)

    }


}