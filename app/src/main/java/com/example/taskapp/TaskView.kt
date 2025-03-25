package com.example.taskapp

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class TaskView : CardView {

    lateinit var root: CardView
    lateinit var title: TextView
    lateinit var dueDate: TextView
    lateinit var isCompleted: CheckBox
    lateinit var priority: TextView
    lateinit var category: TextView
    lateinit var menu: ImageView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context,attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,attrs,defStyleAttr){
        init(context)
    }

    private fun init(context: Context) {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        LayoutInflater.from(context).inflate(R.layout.task_view, this, true)
        root = findViewById(R.id.tv_root)
        title = findViewById(R.id.tv_title)
        dueDate = findViewById(R.id.tv_dueDate)
        isCompleted = findViewById(R.id.tv_isCompleted)
        priority = findViewById(R.id.tv_priority)
        category = findViewById(R.id.tv_category)
        menu = findViewById(R.id.tv_menu)


    }
}