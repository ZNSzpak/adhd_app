package com.example.projekt_inz.ui.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt_inz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoFragment : Fragment() {

    private lateinit var viewModel: ToDoViewModel

    private lateinit var taskList: RecyclerView
    private lateinit var addButton: FloatingActionButton

    private lateinit var taskAdapter: TaskAdapter
    //private val taskListData = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
        val repository = TaskRepository(dao)
        val factory = ToDoViewModelFactory(repository)

        findViews(view)

        viewModel = ViewModelProvider(this, factory)[ToDoViewModel::class.java]

       // initViewModel()
        setupRecyclerView()
        observeViewModel()
        setupAddButton()
//        viewModel.tasks.observe(viewLifecycleOwner) { list ->
//            taskAdapter.submitList(list)
//        }

    }

    private fun findViews(view: View) {
        taskList = view.findViewById(R.id.taskList)
        addButton = view.findViewById(R.id.addButton)
    }

//    private fun initViewModel() {
//
//        val dao = TaskDatabase.getDatabase(requireContext()).taskDao()
//        val repository = TaskRepository(dao)
//        val factory = ToDoViewModelFactory(repository)
//
//        viewModel = ViewModelProvider(this, factory)[ToDoViewModel::class.java]
//    }

    private fun setupRecyclerView() {
//        taskAdapter = TaskAdapter(
//            //taskListData,
//            emptyList(),
//            onEdit = { task, pos -> editTask(task, pos) },
//            onDelete = { _, pos -> viewModel.deleteTask(pos) },
//            onChecked = { _, pos, checked -> viewModel.toggleTask(pos, checked) }
//        )
//
//        taskList.layoutManager = LinearLayoutManager(requireContext())
//        taskList.adapter = taskAdapter
        taskAdapter = TaskAdapter(
            onEdit = { task ->
                EditTaskDialogFragment(task.text) { newText ->
                    viewModel.updateTask(task, newText)
                }.show(parentFragmentManager, "EditTask")
            },
            onDelete = { task -> viewModel.deleteTask(task) },
            onChecked = { task, checked -> viewModel.toggleTask(task, checked) }
        )

        taskList.layoutManager = LinearLayoutManager(requireContext())
        taskList.adapter = taskAdapter
    }


    private fun observeViewModel() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks) //
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            AddTaskDialogFragment { newTaskText ->
                viewModel.addTask(newTaskText)
            }.show(parentFragmentManager, "AddTaskDialog")
        }
    }

//    private fun deleteTask(position: Int) {
//        taskAdapter.removeTask(position)
//    }
//
//    private fun toggleTask(task: Task, position: Int, checked: Boolean) {
//        val updatedTask = task.copy(isDone = checked)
//        taskAdapter.updateTask(position, updatedTask)
//
//    }


}