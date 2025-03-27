package com.example.taskapp

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.TextView
import java.util.UUID

class AddTaskView(screen: TaskAdapter, context: Context){
    private val dialog = Dialog(context)
    private val sqliteManager = SQLiteManager(context)
    init {
        init(screen, context)
    }
    private fun init(screen: TaskAdapter, context: Context) {
        // Set up the dialog
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_task_view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val title = dialog.findViewById<TextView>(R.id.atv_title)
        val description = dialog.findViewById<TextView>(R.id.atv_description)
        val date = dialog.findViewById<TextView>(R.id.atv_date)
        val datePicker = dialog.findViewById<ImageButton>(R.id.imageButton)
        val priority = dialog.findViewById<RadioGroup>(R.id.atv_priority_options)
        val category = dialog.findViewById<TextView>(R.id.atv_category)
        val submit = dialog.findViewById<TextView>(R.id.atv_submit)
        val cancel = dialog.findViewById<TextView>(R.id.atv_cancel)
        // Set the date picker listener to update the date field when a date is selected
        datePicker.setOnClickListener {
            loadCalendarView(context, date)
        }
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
            val tasks = sqliteManager.getAllTasks()
            val sortedTasks = tasks.sortedBy { it.priority }
            screen.submitList(sortedTasks)
            //screen.addTask(model)
            dialog.dismiss()
        }

    }

    fun show() { dialog.show() }

    private fun getPriorityChecked(priority: RadioGroup): Int {
        var selectedPriority = 3
        if (priority.checkedRadioButtonId == R.id.atv_priority_high) {
            selectedPriority = 1
        }
        else if (priority.checkedRadioButtonId == R.id.atv_priority_med) {
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