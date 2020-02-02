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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*

class EditTask : AppCompatActivity() {

    /*companion object{
        const val EXTRA_TASKTITLE="extra_title"
        const val EXTRA_TASKDESC="extra_desc"
        const val EXTRA_TASKDUEDATE="extra_date"
        const val EXTRA_TASKKEY="extra_key"
    }*/

    private lateinit var pageTitle: TextView
    private lateinit var addNewTaskTitle: TextView
    private lateinit var inputTaskTitle: EditText
    private lateinit var addNewTaskDesc: TextView
    private lateinit var inputNewTaskDesc: EditText
    private lateinit var addNewTaskDueDate: TextView
    private lateinit var inputNewTaskDueDate: EditText

    private lateinit var btnSaveChange: Button
    private lateinit var btnDeleteTask: Button
    private lateinit var btnCancel: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var taskKey: String
    private var taskOwner: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        taskKey = intent?.getStringExtra("taskkey").toString()
        taskOwner = intent?.getStringExtra("taskOwner").toString()

        val CircularBlack = Typeface.createFromAsset(assets, "Fonts/CircularStd-Black.otf")
        val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")
        val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")

        pageTitle = findViewById(R.id.add_new_task_title_page)
        pageTitle.typeface = CircularBlack

        addNewTaskTitle = findViewById(R.id.new_task_title)
        addNewTaskTitle.typeface = CircularMedium

        inputTaskTitle = findViewById(R.id.add_task_title)
        inputTaskTitle.typeface = CircularBook
        inputTaskTitle.setText(intent.getStringExtra("tasktitle"))

        addNewTaskDesc = findViewById(R.id.new_task_desc)
        addNewTaskDesc.typeface = CircularMedium

        inputNewTaskDesc = findViewById(R.id.add_task_desc)
        inputNewTaskDesc.typeface = CircularBook
        inputNewTaskDesc.setText(intent.getStringExtra("taskdesc"))

        addNewTaskDueDate = findViewById(R.id.new_task_time)
        addNewTaskDueDate.typeface = CircularMedium

        inputNewTaskDueDate = findViewById(R.id.add_task_time)
        inputNewTaskDueDate.typeface = CircularBook
        inputNewTaskDueDate.setText(intent.getStringExtra("taskdate"))

        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("TaskListApp").child(taskOwner.toString())
            .child("$taskOwner-TaskList").child("Task$taskKey")

        btnSaveChange = findViewById(R.id.btn_save_change)
        btnSaveChange.typeface = CircularMedium
        btnSaveChange.setOnClickListener { v ->
            if (v.id == R.id.btn_save_change) {

                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.ref.child("taskTitle").setValue(inputTaskTitle.text.toString())
                        dataSnapshot.ref.child("taskDesc")
                            .setValue(inputNewTaskDesc.text.toString())
                        dataSnapshot.ref.child("taskDate")
                            .setValue(inputNewTaskDueDate.text.toString())
                        finish()

                        val moveToMain = Intent(this@EditTask, MainActivity::class.java)
                        moveToMain.putExtra("username", taskOwner)
                        startActivity(moveToMain)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(
                            applicationContext,
                            "Action Cancelled, Error Occured",
                            Toast.LENGTH_SHORT
                        ).show()
                        val moveToMain = Intent(this@EditTask, MainActivity::class.java)
                        moveToMain.putExtra("username", taskOwner)
                        startActivity(moveToMain)
                    }
                })

            }
        }
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        btnDeleteTask = findViewById(R.id.btn_delete_task)
        btnDeleteTask.typeface = CircularMedium
        btnDeleteTask.setOnClickListener { v ->
            if (v.id == R.id.btn_delete_task) {

                reference.removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val moveToMain = Intent(this@EditTask, MainActivity::class.java)
                        moveToMain.putExtra("username", taskOwner)
                        startActivity(moveToMain)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to delete task",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

        btnCancel = findViewById(R.id.btn_cancel_edit)
        btnCancel.typeface = CircularMedium
        btnCancel.setOnClickListener { v ->
            if (v.id == R.id.btn_cancel_edit) {
                val moveToMain = Intent(this@EditTask, MainActivity::class.java)
                moveToMain.putExtra("username", taskOwner)
                startActivity(moveToMain)
            }
        }
    }

}
