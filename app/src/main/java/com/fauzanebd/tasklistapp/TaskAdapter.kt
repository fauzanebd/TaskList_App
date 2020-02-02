package com.fauzanebd.tasklistapp

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class TaskAdapter(private val taskList: ArrayList<Task>, assets: AssetManager): RecyclerView.Adapter<TaskAdapter.ListViewHolder>() {

    val CircularBookItalic = Typeface.createFromAsset(assets, "Fonts/CircularStd-BookItalic.otf")
    val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")
    val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")

    lateinit var someContext: Context

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var taskTitle: TextView = itemView.findViewById(R.id.task_title)
        var taskDesc: TextView = itemView.findViewById(R.id.task_desc)
        var taskDate: TextView = itemView.findViewById(R.id.task_date)
    }

    /*interface OnItemClickCallback{
        fun onItemClicked(data: Task)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback=onItemClickCallback
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_tasks, parent, false)
        someContext = parent.context
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val task = taskList[position]

        holder.taskTitle.text = task.taskTitle
        holder.taskDesc.text = task.taskDesc
        holder.taskDate.text = task.taskDate

        holder.taskTitle.typeface = CircularMedium
        holder.taskDesc.typeface = CircularBookItalic
        holder.taskDate.typeface = CircularBook

        /*holder.itemView.setOnClickListener{onItemClickCallback.onItemClicked(taskList[holder.adapterPosition])}*/
        //bikin clickable nya di adapter, ga di main, soalnya nanti jd kacauu
        holder.itemView.setOnClickListener {
            val moveToEdit = Intent(someContext, EditTask::class.java)
            moveToEdit.putExtra("tasktitle", task.taskTitle)
            moveToEdit.putExtra("taskdesc", task.taskDesc)
            moveToEdit.putExtra("taskdate", task.taskDate)
            moveToEdit.putExtra("taskkey", task.taskKey)
            moveToEdit.putExtra("taskOwner", task.taskOwner)
            someContext.startActivity(moveToEdit)
        }

    }
}