package com.example.taskapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var sqliteManager: SQLiteManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view using the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Set the toolbar as the action bar
        setSupportActionBar(binding.toolbar)

        // Initialize the SQLiteManager and get all tasks from the database
        sqliteManager = SQLiteManager(this)
        val tasks = sqliteManager.getAllTasks()
        val sortedTasks = tasks.sortedBy { it.priority }

        // Set up the RecyclerView and adapter
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter = TaskAdapter(this)
        recyclerView.adapter = adapter
        adapter.submitList(sortedTasks)

        // Set up the FAB to open the AddTaskView
        binding.fab.setOnClickListener { view ->
            val addTaskView = AddTaskView(adapter, this)
            addTaskView.show()

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
            R.id.action_clear_screen -> {
                deleteAllTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteAllTasks(){
        // Delete all tasks from the database and the screen
        val tasks = sqliteManager.getAllTasks()
        for (task in tasks) {
            adapter.deleteTask(task)
        }
    }
}