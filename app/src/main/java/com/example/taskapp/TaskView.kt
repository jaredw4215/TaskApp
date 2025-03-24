package com.example.taskapp

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class TaskView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    lateinit var root: CardView
    lateinit var title: TextView
    lateinit var dueDate: TextView
    lateinit var isCompleted: CheckBox
    lateinit var priority: TextView
    lateinit var category: TextView
    lateinit var menu: ImageView

    init {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.task_view, this)
        root = findViewById(R.id.tv_root)
        title = findViewById(R.id.tv_title)
        dueDate = findViewById(R.id.tv_dueDate)
        isCompleted = findViewById(R.id.tv_isCompleted)
        priority = findViewById(R.id.tv_priority)
        category = findViewById(R.id.tv_category)
        menu = findViewById(R.id.tv_menu)


    }
}