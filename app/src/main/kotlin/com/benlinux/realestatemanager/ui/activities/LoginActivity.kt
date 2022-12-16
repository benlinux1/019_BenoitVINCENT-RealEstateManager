package com.benlinux.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.benlinux.realestatemanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase


class LoginActivity : AppCompatActivity() {


    private lateinit var emailId: EditText
    private lateinit var password:EditText
    private lateinit var loginButton: Button
    private lateinit var signUpLink: TextView

    var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null

    private var mAuthStateListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailId = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signUpLink = findViewById(R.id.login_signup_link)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = AuthStateListener { firebaseAuth ->
            val mFirebaseUser = firebaseAuth.currentUser
            if (mFirebaseUser != null) {
                navigateToHomeActivity()
            } else {
                Toast.makeText(this, "Please login", Toast.LENGTH_LONG).show()
            }
        }
        loginButton.setOnClickListener {
                val email = emailId.text.toString()
                val pwd: String = password.text.toString()

                if (email.isEmpty()) {
                    emailId.error = "Please provide email"
                    emailId.requestFocus()
                } else if (pwd.isEmpty()) {
                    password.error = "Please provide password"
                    password.requestFocus()
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(this, "Fields are empty", Toast.LENGTH_LONG).show()
                } else {
                    if (!(email.isEmpty() && pwd.isEmpty())) {
                        firebaseAuth!!.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(this) { task ->
                                if (!task.isSuccessful) {
                                    Toast.makeText(
                                        this, "Login Error ,Please Login In",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_LONG).show()
                                    navigateToHomeActivity()
                                }
                            }
                    } else {
                    Toast.makeText(this, "Error Occurred !", Toast.LENGTH_LONG).show()
                }
            }
        }
        signUpLink.setOnClickListener {
            val signUpIntent = Intent(this, SignupActivity::class.java)
            startActivity(signUpIntent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    private fun navigateToHomeActivity() {
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

}