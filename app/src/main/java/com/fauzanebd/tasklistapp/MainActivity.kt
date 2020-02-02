package com.fauzanebd.tasklistapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    private lateinit var titlePage: TextView
    private lateinit var subtitlePage: TextView
    private lateinit var endPage: TextView
    private lateinit var rvTasks: RecyclerView
    private var list: ArrayList<Task> = arrayListOf()
    private lateinit var reference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var btnAddNew: Button
    private lateinit var btnSignOut: Button
    private lateinit var btnDeleteAcc: Button

    private var currentUser: String? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUser = intent.getStringExtra("username")
        if (currentUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        titlePage = findViewById(R.id.title_page)
        subtitlePage = findViewById(R.id.subtitle_page)
        endPage = findViewById(R.id.end_page)

        btnAddNew = findViewById(R.id.btn_addnew)
        btnSignOut = findViewById(R.id.btn_sign_out)
        btnDeleteAcc = findViewById(R.id.btn_delete_account)

        rvTasks = findViewById(R.id.rv_taskList)
        rvTasks.setHasFixedSize(true)
        rvTasks.layoutManager = LinearLayoutManager(this)
        database = FirebaseDatabase.getInstance()

        reference = database.reference.child("TaskListApp").child(currentUser.toString())
            .child("$currentUser-TaskList")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    var task: Task? = dataSnapshot1.getValue<Task>(Task::class.java)
                    if (task != null) {
                        list.add(task)
                    }
                }
                //list dari database di daftarin ke taskAdapter
                val taskAdapter = TaskAdapter(list, assets)
                rvTasks.adapter = taskAdapter
                taskAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext, "No Data", Toast.LENGTH_SHORT).show()
            }
        })

        val CircularBlack = Typeface.createFromAsset(assets, "Fonts/CircularStd-Black.otf")
//        val CircularBlackItalic = Typeface.createFromAsset(assets, "Fonts/CircularStd-BlackItalic.otf")
//        val CircularBold = Typeface.createFromAsset(assets, "Fonts/CircularStd-Bold.otf")
        val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")
        val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")
        val CircularMediumItalic =
            Typeface.createFromAsset(assets, "Fonts/CircularStd-MediumItalic.otf")


        titlePage.typeface = CircularBlack
        subtitlePage.typeface = CircularMedium
        endPage.typeface = CircularMediumItalic

        //daftarin tombol bulet
        btnAddNew.typeface = CircularBook
        btnAddNew.setOnClickListener { v ->
            if (v.id == R.id.btn_addnew) {
                val moveToAddNew = Intent(this@MainActivity, AddNewTask::class.java)
                moveToAddNew.putExtra("currentUser", currentUser)
                endPage.visibility = View.GONE
                startActivity(moveToAddNew)
            }
        }

        btnSignOut.typeface = CircularMedium
        btnSignOut.setOnClickListener { v ->
            if (v.id == R.id.btn_sign_out) {
                currentUser = null
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }

        btnDeleteAcc.typeface = CircularMedium
        btnDeleteAcc.setOnClickListener { v ->
            if (v.id == R.id.btn_delete_account) {
                reference = database.reference.child("TaskListApp").child(currentUser.toString())
                reference.removeValue().addOnCompleteListener {
                    if (it.isSuccessful) {
                        val moveToLogin = Intent(this@MainActivity, LoginActivity::class.java)
                        Toast.makeText(
                            applicationContext,
                            "Your account has been successfully deleted",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(moveToLogin)
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

    }

}
