package com.example.taskapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteManager(context: Context): SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_TASKS (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                DUE_DATE + " TEXT, " +
                IS_COMPLETED + " INTEGER, " +
                PRIORITY + " TEXT, " +
                CATEGORY + " TEXT)"
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_TASKS"
        p0?.execSQL(dropTable)
        onCreate(p0)
    }

    companion object{
        private const val DATABASE_NAME = "TaskApp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TASKS = "Tasks"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val DUE_DATE = "dueDate"
        private const val IS_COMPLETED = "isCompleted"
        private const val PRIORITY = "priority"
        private const val CATEGORY = "category"

    }

    fun insertTask(task: TaskModel): Long{
        val values = ContentValues()
        val db = this.writableDatabase
        values.put(ID, task.id)
        values.put(TITLE, task.title)
        values.put(DESCRIPTION, task.description)
        values.put(DUE_DATE, task.dueDate)
        values.put(IS_COMPLETED, task.isCompleted)
        values.put(PRIORITY, task.priority)
        values.put(CATEGORY, task.category)
        val success = db.insert(TABLE_TASKS, null, values)
        db.close()
        return success
    }

    fun updateTask(task: TaskModel): Int{
        val values = ContentValues()
        val db = this.writableDatabase
        values.put(ID, task.id)
        values.put(TITLE, task.title)
        values.put(DESCRIPTION, task.description)
        values.put(DUE_DATE, task.dueDate)
        values.put(IS_COMPLETED, task.isCompleted)
        values.put(PRIORITY, task.priority)
        values.put(CATEGORY, task.category)
        val success = db.update(TABLE_TASKS, values, "$ID = ${task.id}", null)
        db.close()
        return success
    }

    fun deleteTask(task: TaskModel): Int{
        val db = this.writableDatabase
        val success = db.delete(TABLE_TASKS, "$ID = ${task.id}", null)
        db.close()
        return success
    }

    fun getAllTasks(): List<TaskModel> {
        val taskList = mutableListOf<TaskModel>()
        val selectQuery = "SELECT * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return mutableListOf()
        }
        var id: String
        var title: String
        var description: String
        var dueDate: String
        var isCompleted: Boolean
        var priority: String
        var category: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(cursor.getColumnIndexOrThrow(ID))
                title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                dueDate = cursor.getString(cursor.getColumnIndexOrThrow(DUE_DATE))
                isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(IS_COMPLETED)) == 1
                priority = cursor.getString(cursor.getColumnIndexOrThrow(PRIORITY))
                category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY))
                val task = TaskModel(id, title, description, dueDate, isCompleted, priority, category)
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }
}