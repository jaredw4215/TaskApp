package com.example.taskapp

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.Window
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import java.util.UUID

class AddTaskView(context: Context, screen: LinearLayout, sqliteManager: SQLiteManager){
    val dialog = Dialog(context)
    init {
        init(context, screen, sqliteManager)
    }
    private fun init(context: Context, screen: LinearLayout, sqliteManager: SQLiteManager) {
        // Set up the dialog
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_task_view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val title = dialog.findViewById<TextView>(R.id.atv_title)
        val description = dialog.findViewById<TextView>(R.id.atv_description)
        val date = dialog.findViewById<TextView>(R.id.atv_date)
        val priority = dialog.findViewById<RadioGroup>(R.id.atv_priority_options)
        val category = dialog.findViewById<TextView>(R.id.atv_category)
        val submit = dialog.findViewById<TextView>(R.id.atv_submit)
        val cancel = dialog.findViewById<TextView>(R.id.atv_cancel)

        // Set the listeners for the cancel and submit buttons
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        submit.setOnClickListener {
            // Create a new task model with the values from the dialog fields
            val model = TaskModel(
                id = UUID.randomUUID().hashCode(),
                title = title.text.toString(),
                description = description.text.toString(),
                dueDate = date.text.toString(),
                priority = getPriorityChecked(priority),
                category = category.text.toString()
            )

            // Add the new task to the database and the screen
            sqliteManager.insertTask(model)
            addTaskToScreen(context, screen, model, sqliteManager)
            dialog.dismiss()
        }

    }

    fun show() { dialog.show() }

    private fun getPriorityChecked(priority: RadioGroup): String {
        var selectedPriority = "Low"
        if (priority.checkedRadioButtonId == R.id.atv_priority_high) {
            selectedPriority = "High"
        }
        else if (priority.checkedRadioButtonId == R.id.atv_priority_med) {
            selectedPriority = "Medium"
        }
        return selectedPriority

    }

    companion object {
        fun addTaskToScreen(
            context: Context,
            screen: LinearLayout,
            model: TaskModel,
            sqliteManager: SQLiteManager
        ) {
            // Create a new task view and set its properties with the values from the task model
            val taskView = TaskView(context)
            taskView.title.text = model.title
            taskView.dueDate.text = model.dueDate
            taskView.priority.text = model.priority
            taskView.category.text = model.category
            taskView.isCompleted.isChecked = model.isCompleted

            // Update the task view's background color based on its completion status
            updateTaskBackground(taskView, model, model.isCompleted)
            taskView.isCompleted.setOnCheckedChangeListener { _, isChecked ->
                // Update the task model's completion status when the checkbox is checked or unchecked
                updateTaskBackground(taskView, model, isChecked)
                sqliteManager.updateTask(model)
            }
            taskView.menu.setOnClickListener {
                // Show a popup menu with options to edit and delete the task
                val popupMenu = createPopup(context, taskView, model, sqliteManager, screen)
                popupMenu.show()
            }
            screen.addView(taskView)
        }

        private fun createPopup(
            context: Context,
            taskView: TaskView,
            model: TaskModel,
            sqliteManager: SQLiteManager,
            screen: LinearLayout
        ): PopupMenu {
            // Create a popup menu with options to edit and delete the task
            val popupMenu = PopupMenu(context, taskView.menu)
            popupMenu.inflate(R.menu.menu_task)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item1 -> {
                        // Show the edit task view when the edit option is selected
                        val editTaskView = EditTaskView(context, taskView, model, sqliteManager)
                        editTaskView.show()
                        true
                    }

                    R.id.item2 -> {
                        // Delete the task when the delete option is selected
                        deleteTask(taskView, screen, model, sqliteManager)
                        true
                    }

                    else -> false
                }
            }
            return popupMenu
        }

        private fun updateTaskBackground(taskView: TaskView, model: TaskModel, isChecked: Boolean) {
            if (isChecked) {
                // Set the background color of the task view to gray if it is completed
                taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.gray_400))
                model.isCompleted = true
            } else {
                // Set the background color of the task view to white if it is not completed
                taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.white))
                model.isCompleted = false
            }
        }

        private fun deleteTask(
            view: TaskView,
            screen: LinearLayout,
            model: TaskModel,
            sqliteManager: SQLiteManager
        ) {
            // Delete the task from the database and the screen when the delete option is selected
            sqliteManager.deleteTask(model)
            screen.removeView(view)

        }
    }

}