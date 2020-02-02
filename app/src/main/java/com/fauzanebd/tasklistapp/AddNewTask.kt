package com.fauzanebd.tasklistapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import kotlin.random.Random

class AddNewTask : AppCompatActivity(), View.OnClickListener {

    private lateinit var pageTitle: TextView
    private lateinit var addNewTaskTitle: TextView
    private lateinit var inputTaskTitle: EditText
    private lateinit var addNewTaskDesc: TextView
    private lateinit var inputNewTaskDesc: EditText
    private lateinit var addNewTaskDueDate: TextView
    private lateinit var inputNewTaskDueDate: EditText
    private lateinit var btnCreateTask: Button
    private lateinit var btnCancel: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var taskOwner: String? = null
    var taskNum: Int = Random.nextInt()

    private var taskKey: String = taskNum.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        taskOwner = intent.getStringExtra("currentUser")
        val CircularBlack = Typeface.createFromAsset(assets, "Fonts/CircularStd-Black.otf")
        val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")
        val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")

        pageTitle=findViewById(R.id.add_new_task_title_page)
        pageTitle.typeface=CircularBlack

        addNewTaskTitle=findViewById(R.id.new_task_title)
        addNewTaskTitle.typeface=CircularMedium

        inputTaskTitle=findViewById(R.id.add_task_title)
        inputTaskTitle.typeface=CircularBook

        addNewTaskDesc=findViewById(R.id.new_task_desc)
        addNewTaskDesc.typeface=CircularMedium

        inputNewTaskDesc=findViewById(R.id.add_task_desc)
        inputNewTaskDesc.typeface=CircularBook

        addNewTaskDueDate=findViewById(R.id.new_task_time)
        addNewTaskDueDate.typeface=CircularMedium

        inputNewTaskDueDate=findViewById(R.id.add_task_time)
        inputNewTaskDueDate.typeface=CircularBook

        btnCreateTask=findViewById(R.id.btn_save_task)
        btnCreateTask.typeface=CircularMedium
        btnCreateTask.setOnClickListener(this)


        btnCancel=findViewById(R.id.btn_cancel)
        btnCancel.typeface=CircularMedium
        btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_save_task -> {
                database = FirebaseDatabase.getInstance()
                reference = database.reference.child("TaskListApp").child(taskOwner.toString()).child("$taskOwner-TaskList").child("Task$taskNum")
                reference.addListenerForSingleValueEvent(object: ValueEventListener{

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("taskTitle")
                            .setValue(inputTaskTitle.text.toString())
                        dataSnapshot.ref.child("taskDesc")
                            .setValue(inputNewTaskDesc.text.toString())
                        dataSnapshot.ref.child("taskDate")
                            .setValue(inputNewTaskDueDate.text.toString())
                        dataSnapshot.ref.child("taskKey").setValue(taskKey)
                        dataSnapshot.ref.child("taskOwner").setValue(taskOwner)
                        val moveToMain = Intent(this@AddNewTask, MainActivity::class.java)
                        moveToMain.putExtra("username", taskOwner)
                        startActivity(moveToMain)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Action Cancelled", Toast.LENGTH_SHORT).show()
                        val moveToMain = Intent(this@AddNewTask, MainActivity::class.java)
                        moveToMain.putExtra("username", taskOwner)
                        startActivity(moveToMain)
                    }
                })
            }
            R.id.btn_cancel -> {
                val moveToMain = Intent(this@AddNewTask, MainActivity::class.java)
                moveToMain.putExtra("username", taskOwner)
                startActivity(moveToMain)
            }
        }
    }
}
