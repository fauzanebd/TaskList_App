package com.fauzanebd.tasklistapp

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var userNameInputTitle: TextView
    private lateinit var passwordInputTitle: TextView
    private lateinit var pageTitle: TextView
    private lateinit var pageSubtitle: TextView
    private lateinit var userNameInput: EditText
    private lateinit var passWordInput: EditText

    private var isUserExist: Boolean = false
    //    private var isUsernameExist: Boolean = false
    private var isPasswordTrue: Boolean = false

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var userReference: DatabaseReference
    //    private lateinit var nameReference: DatabaseReference
    private lateinit var passReference: DatabaseReference

    private lateinit var btnSignIn: Button
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val CircularBlack = Typeface.createFromAsset(assets, "Fonts/CircularStd-Black.otf")
        val CircularBook = Typeface.createFromAsset(assets, "Fonts/CircularStd-Book.otf")
        val CircularMedium = Typeface.createFromAsset(assets, "Fonts/CircularStd-Medium.otf")

        userNameInputTitle = findViewById(R.id.usernameTitle)
        userNameInputTitle.typeface = CircularMedium
        passwordInputTitle = findViewById(R.id.passwordTitle)
        passwordInputTitle.typeface = CircularMedium
        pageTitle = findViewById(R.id.login_title_page)
        pageTitle.typeface = CircularBlack
        pageSubtitle = findViewById(R.id.login_subtitle_page)
        pageSubtitle.typeface = CircularMedium
        userNameInput = findViewById(R.id.input_username)
        userNameInput.typeface = CircularBook
        passWordInput = findViewById(R.id.input_passsword)
        passWordInput.typeface = CircularBook

        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("TaskListApp")

        btnSignIn = findViewById(R.id.btn_sign_in)
        btnSignIn.typeface = CircularMedium
        btnSignIn.setOnClickListener(this)

        btnSignUp = findViewById(R.id.btn_sign_up)
        btnSignUp.typeface = CircularMedium
        btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_sign_in) {
            if (userNameInput.text.isEmpty()) {
                userNameInput.error = "Username tidak boleh kosong"
            } else if (passWordInput.text.isEmpty()) {
                passWordInput.error = "Password tidak boleh kosong"
            } else {
                userReference = reference.child(userNameInput.text.toString())
                userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            isUserExist = true
                        }
                        if (isUserExist) {
                            passReference = userReference.child("${userNameInput.text}-UserId")
                            passReference.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    var realPass = p0.child("userPassword").value
                                    if (passWordInput.text.toString() == realPass.toString()) {
                                        isPasswordTrue = true
                                        if (isPasswordTrue) {
                                            val moveToMain =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            moveToMain.putExtra(
                                                "username",
                                                userNameInput.text.toString()
                                            )
                                            startActivity(moveToMain)
                                        } else {
                                            passWordInput.error = "Kata sandi tidak valid"
                                        }
                                    }
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            })
                        } else {
                            userNameInput.error = "Username tidak ditemukan"
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        if (v.id == R.id.btn_sign_up) {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }
    }
}
