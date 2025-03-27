package com.example.taskapp

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView

class EditTaskView(context: Context, screen: TaskAdapter, model: TaskModel){
    private val dialog = Dialog(context)
    private val sqliteManager = SQLiteManager(context)
    init {
        init(screen, model, context)
    }
    private fun init(screen: TaskAdapter, model: TaskModel, context: Context) {
        // Set up the dialog
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.edit_task_view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val title = dialog.findViewById<TextView>(R.id.etv_title)
        val description = dialog.findViewById<TextView>(R.id.etv_description)
        val date = dialog.findViewById<TextView>(R.id.etv_date)
        val datePicker = dialog.findViewById<ImageButton>(R.id.imageButton)
        val priority = dialog.findViewById<RadioGroup>(R.id.etv_priority_options)
        val category = dialog.findViewById<TextView>(R.id.etv_category)
        val submit = dialog.findViewById<TextView>(R.id.etv_submit)
        val cancel = dialog.findViewById<TextView>(R.id.etv_cancel)
        // Set the date picker listener to update the date field when a date is selected
        datePicker.setOnClickListener {
            loadCalendarView(context, date)
        }
        // Set the existing task details in the dialog fields
        title.text = model.title
        description.text = model.description
        date.text = model.dueDate
        // Set the priority radio button based on the task priority
        if (model.priority == 1) {
            priority.check(R.id.etv_priority_high)
        }
        else if (model.priority == 2) {
            priority.check(R.id.etv_priority_med)
        }
        else {
            priority.check(R.id.etv_priority_low)
        }
        category.text = model.category

        // Set the listeners for the cancel and submit buttons
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        submit.setOnClickListener {
            // Check if the title field is empty and show an error message if it is
            if (title.text.toString().isEmpty()) {
                title.error = "Please enter a title"
                return@setOnClickListener
            }
            // Update the task details with the new values from the dialog fields
            model.title = title.text.toString()
            model.description = description.text.toString()
            model.dueDate = date.text.toString()
            model.priority = getPriorityChecked(priority)
            model.category = category.text.toString()

            // Update the task in the database and update the task view on the screen
            sqliteManager.updateTask(model)
            val tasks = sqliteManager.getAllTasks()
            val sortedTasks = tasks.sortedBy { it.priority }
            screen.submitList(sortedTasks)
            //screen.updateTask(model)
            dialog.dismiss()
        }

    }

    fun show() { dialog.show() }

    private fun getPriorityChecked(priority: RadioGroup): Int {
        var selectedPriority = 3
        if (priority.checkedRadioButtonId == R.id.etv_priority_high) {
            selectedPriority = 1
        }
        else if (priority.checkedRadioButtonId == R.id.etv_priority_med) {
            selectedPriority = 2
        }
        return selectedPriority

    }

    private fun loadCalendarView(context: Context, date: TextView) {
        val datePickerDialog = Dialog(context)
        datePickerDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        datePickerDialog.setCancelable(true)
        datePickerDialog.setContentView(R.layout.calendar_view)
        val datePickerView = datePickerDialog.findViewById<CalendarView>(R.id.calendarView)

        datePickerView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date.text = buildString {
                append(month+1)
                append("/")
                append(dayOfMonth)
                append("/")
                append(year)
            }
            datePickerDialog.dismiss()
        }

        datePickerDialog.show()
    }

}