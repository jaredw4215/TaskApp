package com.example.taskapp

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter (private val context: Context,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val tasks: MutableList<TaskModel> = mutableListOf()
    private val sqliteManager = SQLiteManager(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false))
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder){
            is TaskViewHolder -> {
                holder.bind(tasks[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun submitList(tasklist: List<TaskModel>) {
        tasks.clear()
        tasks.addAll(tasklist)
        notifyDataSetChanged()

    }

    fun deleteTask(model: TaskModel) {
        // Get the position of the task in the list
        val adapterPosition = tasks.indexOf(model)
        // Delete the task from the database and the screen when the delete option is selected
        sqliteManager.deleteTask(model)
        tasks.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
        notifyItemRangeChanged(adapterPosition, tasks.size)

    }

    inner class TaskViewHolder(taskView: View): RecyclerView.ViewHolder(taskView){
        // Initialize the views in the task view
        val title = taskView.findViewById<TextView>(R.id.tv_title)
        val dueDate = taskView.findViewById<TextView>(R.id.tv_dueDate)
        val priority = taskView.findViewById<TextView>(R.id.tv_priority)
        val category = taskView.findViewById<TextView>(R.id.tv_category)
        val isCompleted = taskView.findViewById<CheckBox>(R.id.tv_isCompleted)
        val menu = taskView.findViewById<ImageView>(R.id.tv_menu)
        val root = taskView.findViewById<CardView>(R.id.tv_root)
        val resources = taskView.resources

        // Bind the task data to the task view
        fun bind(task: TaskModel){
            title.text = task.title
            dueDate.text = task.dueDate
            if (task.priority == 1) {
                priority.text = buildString { append("High") }
            } else if (task.priority == 2) {
                priority.text = buildString { append("Medium") }
            } else {
                priority.text = buildString { append("Low") }
            }
            category.text = task.category
            isCompleted.isChecked = task.isCompleted

            // Set the background color and strikethrough of the task view based on its completion status
            updateTaskBackground(root, resources, task, task.isCompleted)
            isCompleted.setOnCheckedChangeListener { _, isChecked ->
                // Update the task model's completion status when the checkbox is checked or unchecked
                updateTaskBackground(root, resources, task, isChecked)
                sqliteManager.updateTask(task)
            }
            menu.setOnClickListener {
                // Show a popup menu with options to edit and delete the task
                val popupMenu = createPopup(context, menu, task)
                popupMenu.show()
            }
            root.setOnClickListener {
                // Show the description of the task when the task view is clicked
                showDescription(context, task.description)
            }
        }
        private fun createPopup(
            context: Context,
            menu: ImageView,
            model: TaskModel,
        ): PopupMenu {
            // Create a popup menu with options to edit and delete the task
            val popupMenu = PopupMenu(context, menu)
            popupMenu.inflate(R.menu.menu_task)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item1 -> {
                        // Show the edit task view when the edit option is selected
                        val editTaskView = EditTaskView(context,this@TaskAdapter, model)
                        editTaskView.show()
                        true
                    }
                    R.id.item2 -> {
                        // Delete the task when the delete option is selected
                        deleteTask(model)
                        true
                    }
                    else -> false
                }
            }
            return popupMenu
        }

        private fun updateTaskBackground(root: CardView,resources: Resources, model: TaskModel, isChecked: Boolean) {
            if (isChecked) {
                // Set the background color of the task view to gray if it is completed
                root.setCardBackgroundColor(resources.getColor(R.color.gray_400))
                // strikethrough the task title
                title.paintFlags = title.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                // Update the task model's completion status to true
                model.isCompleted = true
            } else {
                // Set the background color of the task view to white if it is not completed
                root.setCardBackgroundColor(resources.getColor(R.color.white))
                // remove the strikethrough from the task title
                title.paintFlags = title.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                // Update the task model's completion status to false
                model.isCompleted = false
            }
        }

        private fun showDescription(context: Context, desc: String?) {
            // Show the description of the task when the task view is clicked
            val dialog = Dialog(context)
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.description_view)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val heading = dialog.findViewById<TextView>(R.id.dv_title)
            val description = dialog.findViewById<TextView>(R.id.dv_content)
            heading.text = title.text
            description.text = desc
            dialog.show()

        }


    }
}

