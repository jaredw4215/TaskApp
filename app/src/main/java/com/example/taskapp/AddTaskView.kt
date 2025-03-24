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

class AddTaskView(context: Context, screen: LinearLayout, sqliteManager: SQLiteManager){
    val dialog = Dialog(context)
    init {
        init(context, screen, sqliteManager)
    }
    private fun init(context: Context, screen: LinearLayout, sqliteManager: SQLiteManager) {
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
            val model = TaskModel(
                title = title.text.toString(),
                description = description.text.toString(),
                dueDate = date.text.toString(),
                //isCompleted = false,
                priority = selectedPriority,
                category = category.text.toString()
            )
            sqliteManager.insertTask(model)
            MainActivity.addTaskToScreen(context, screen, model, sqliteManager)
            //addTaskToScreen(context, screen, model)
            dialog.dismiss()
        }

    }
    fun show() {
        dialog.show()
    }
    private fun addTaskToScreen(context: Context, screen: LinearLayout, model: TaskModel) {
        val taskView = TaskView(context)
        taskView.title.text = model.title
        taskView.dueDate.text = model.dueDate
        taskView.priority.text = model.priority
        taskView.category.text = model.category
        taskView.isCompleted.isChecked = model.isCompleted
        screen.addView(taskView)

    }

}