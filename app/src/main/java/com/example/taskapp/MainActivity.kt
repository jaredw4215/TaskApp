package com.example.taskapp

import android.content.Context
import android.os.Bundle
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import com.example.taskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var linearLayout: LinearLayout
    private lateinit var sqliteManager: SQLiteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        linearLayout = findViewById<LinearLayout>(R.id.linearlayout)
        sqliteManager = SQLiteManager(this)

        binding.fab.setOnClickListener { view ->
            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //    .setAction("Action", null)
            //    .setAnchorView(R.id.fab).show()
            val addTaskView = AddTaskView(this, linearLayout, sqliteManager)
            addTaskView.show()

        }


        val tasks = sqliteManager.getAllTasks()
        for (task in tasks) {
            addTaskToScreen(this, linearLayout, task, sqliteManager)
        }

    }
    companion object {
        fun addTaskToScreen(context: Context, screen: LinearLayout, model: TaskModel, sqliteManager: SQLiteManager) {
            val taskView = TaskView(context)
            taskView.title.text = model.title
            taskView.dueDate.text = model.dueDate
            taskView.priority.text = model.priority
            taskView.category.text = model.category
            taskView.isCompleted.isChecked = model.isCompleted
            if (taskView.isCompleted.isChecked) {
                taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.gray_400))
            }else{
                taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.white))
            }
            taskView.isCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.gray_400))
                    model.isCompleted = true
                } else {
                    taskView.root.setCardBackgroundColor(taskView.resources.getColor(R.color.white))
                    model.isCompleted = false
                }
                sqliteManager.updateTask(model)
            }
            taskView.menu.setOnClickListener {
                val popupMenu = PopupMenu(context, taskView.menu)
                popupMenu.inflate(R.menu.menu_task)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item1 -> {
                            val editTaskView = EditTaskView(context, taskView, model)
                            editTaskView.show()
                            true
                        }

                        R.id.item2 -> {
                            deleteTask(taskView, screen, model, sqliteManager)
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
            screen.addView(taskView)
        }
        fun deleteTask(view: TaskView, screen: LinearLayout, model: TaskModel, sqliteManager: SQLiteManager) {
            sqliteManager.deleteTask(model)
            screen.removeView(view)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}