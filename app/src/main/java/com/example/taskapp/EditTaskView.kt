package com.example.taskapp

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.Window
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.cardview.widget.CardView

class EditTaskView(context: Context, view: TaskView, model: TaskModel, sqliteManager: SQLiteManager){
    val dialog = Dialog(context)
    init {
        init(view, model, sqliteManager)
    }
    private fun init(view: TaskView, model: TaskModel, sqliteManager: SQLiteManager) {
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

        // Set the existing task details in the dialog fields
        title.text = model.title
        description.text = model.description
        date.text = model.dueDate
        category.text = model.category

        // Set the listeners for the cancel and submit buttons
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        submit.setOnClickListener {
            // Update the task details with the new values from the dialog fields
            model.title = title.text.toString()
            model.description = description.text.toString()
            model.dueDate = date.text.toString()
            model.priority = getPriorityChecked(priority)
            model.category = category.text.toString()

            // Update the task in the database and update the task view on the screen
            sqliteManager.updateTask(model)
            updateTaskOnScreen(view, model)
            dialog.dismiss()
        }

    }

    fun show() { dialog.show() }

    private fun updateTaskOnScreen(taskView: TaskView, model: TaskModel) {
        // Update the task view on the screen with the new task details
        taskView.title.text = model.title
        taskView.dueDate.text = model.dueDate
        taskView.priority.text = model.priority
        taskView.category.text = model.category
        taskView.isCompleted.isChecked = model.isCompleted

    }

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

}