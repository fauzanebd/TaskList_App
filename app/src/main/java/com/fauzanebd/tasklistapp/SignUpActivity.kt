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

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var signUpTitle: TextView
    private lateinit var signUpSubtitle: TextView
    private lateinit var inputNameTitle: TextView
    private lateinit var inputUsernameTitle: TextView
    private lateinit var inputPasswordTitle: TextView

    private lateinit var inputName: EditText
    private lateinit var inputUserName: EditText
    private lateinit var inputPassword: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var btnSignUp: Button
    private lateinit var btnCancel: Button

    private var isUserExist: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val CircularBlack = Typeface.createFromAsset(assets, "Fonts/CircularStd-Black.otf")
        val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")
        val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")

        signUpTitle = findViewById(R.id.sign_up_title_page)
        signUpTitle.typeface = CircularBlack

        signUpSubtitle = findViewById(R.id.sign_up_subtitle_page)
        signUpSubtitle.typeface = CircularMedium

        inputNameTitle = findViewById(R.id.registerNameTitle)
        inputNameTitle.typeface = CircularMedium

        inputUsernameTitle = findViewById(R.id.registerusernameTitle)
        inputUsernameTitle.typeface = CircularMedium

        inputPasswordTitle = findViewById(R.id.registerpasswordTitle)
        inputPasswordTitle.typeface = CircularMedium

        inputName = findViewById(R.id.register_name)
        inputUserName = findViewById(R.id.register_username)
        inputPassword = findViewById(R.id.register_password)

        inputName.typeface = CircularBook
        inputUserName.typeface = CircularBook
        inputPassword.typeface = CircularBook

        btnSignUp = findViewById(R.id.btn_sign_up)
        btnSignUp.typeface = CircularMedium
        btnSignUp.setOnClickListener(this)

        btnCancel = findViewById(R.id.btn_cancel)
        btnCancel.typeface = CircularMedium
        btnCancel.setOnClickListener(this)

        database = FirebaseDatabase.getInstance()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_sign_up) {
            if(inputName.text.isEmpty()){
                inputName.error="Nama tidak boleh kosong"
            } else if(inputUserName.text.isEmpty()) {
                inputUserName.error="Username tidak boleh kosong"
            } else if(inputPassword.text.isEmpty()){
                inputPassword.error="Password tidak boleh kosong"
            } else {
                reference =
                    database.reference.child("TaskListApp").child(inputUserName.text.toString())
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            isUserExist = true
                        }
                        if (!isUserExist) {
                            reference = database.reference.child("TaskListApp")
                                .child(inputUserName.text.toString())
                            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.ref.child("${inputUserName.text}-UserId")
                                        .child("nameOfUser")
                                        .setValue(inputName.text.toString())
                                    dataSnapshot.ref.child("${inputUserName.text}-UserId")
                                        .child("userPassword")
                                        .setValue(inputPassword.text.toString())
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Action Cancelled",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        } else {
                            inputUserName.error = "Username sudah ada yang punya"
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        if (v.id == R.id.btn_cancel) {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
        }
    }
}
