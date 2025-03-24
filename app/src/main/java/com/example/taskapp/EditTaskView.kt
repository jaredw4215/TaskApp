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

class EditTaskView(context: Context, view: TaskView, model: TaskModel){
    val dialog = Dialog(context)
    var sqliteManager: SQLiteManager = SQLiteManager(context)
    init {
        init(view, model)
    }
    private fun init(view: TaskView, model: TaskModel) {
        dialog.setTitle("Add Task")
        dialog.setCancelable(true)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_task_view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val title = dialog.findViewById<TextView>(R.id.atv_title)
        val description = dialog.findViewById<TextView>(R.id.atv_description)
        val date = dialog.findViewById<TextView>(R.id.atv_date)
        val priority = dialog.findViewById<RadioGroup>(R.id.atv_priority_options)
        val category = dialog.findViewById<TextView>(R.id.atv_category)
        val submit = dialog.findViewById<TextView>(R.id.atv_submit)
        val cancel = dialog.findViewById<TextView>(R.id.atv_cancel)

        title.text = model.title
        description.text = model.description
        date.text = model.dueDate
        category.text = model.category
        if (model.priority == "High") {
            priority.check(R.id.atv_priority_high)
        }
        else if (model.priority == "Medium") {
            priority.check(R.id.atv_priority_med)
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        submit.setOnClickListener {
            var selectedPriority = "Low"
            if (priority.checkedRadioButtonId == R.id.atv_priority_low) {
                selectedPriority = "Low"
            }
            else if (priority.checkedRadioButtonId == R.id.atv_priority_high) {
                selectedPriority = "High"
            }
            else if (priority.checkedRadioButtonId == R.id.atv_priority_med) {
                selectedPriority = "Medium"
            }
            model.title = title.text.toString()
            model.description = description.text.toString()
            model.dueDate = date.text.toString()
            model.priority = selectedPriority
            model.category = category.text.toString()

            sqliteManager.updateTask(model)
            updateTaskOnScreen(view, model)
            dialog.dismiss()
        }

    }
    fun show() {
        dialog.show()
    }
    private fun updateTaskOnScreen(taskView: TaskView, model: TaskModel) {
        taskView.title.text = model.title
        taskView.dueDate.text = model.dueDate
        taskView.priority.text = model.priority
        taskView.category.text = model.category
        taskView.isCompleted.isChecked = model.isCompleted

    }

}