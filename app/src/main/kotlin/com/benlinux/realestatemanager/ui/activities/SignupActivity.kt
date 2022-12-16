package com.benlinux.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignupActivity : AppCompatActivity(){

    private lateinit var emailId: EditText
    private lateinit var firstName:EditText
    private lateinit var lastName:EditText
    private lateinit var password:EditText
    private lateinit var repeatPassword:EditText
    private lateinit var signUpButton: Button
    private lateinit var signInLink: TextView

    var mFirebaseAuth: FirebaseAuth? = null
    private lateinit var firebaseDatabase: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setViews()
        setFirebaseDatabase()
        setListenerOnSignUpButton()
        setListenerOnSignInLink()

    }

    private fun setViews() {
        emailId = findViewById(R.id.signup_email)
        firstName = findViewById(R.id.signup_firstName)
        lastName = findViewById(R.id.signup_lastName)
        password = findViewById(R.id.signup_password)
        repeatPassword = findViewById(R.id.signup_repeat_password)
        signUpButton = findViewById(R.id.signup_button)
        signInLink = findViewById(R.id.signup_signIn_link)
    }

    private fun setFirebaseDatabase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    private fun checkIfPasswordsAreEquals(): Boolean {
        return (password.text.toString() === repeatPassword.text.toString())
    }

    private fun setListenerOnSignUpButton() {
        signUpButton.setOnClickListener {
            // Define user data
            val email = emailId.text.toString()
            val userFirstName: String = firstName.text.toString()
            val userLastName: String = lastName.text.toString()
            val userPassword: String = password.text.toString()
            var passwordAreEquals = checkIfPasswordsAreEquals()

            // Check errors
            if (email.isEmpty()) {
                emailId.error = "Please provide email id"
                emailId.requestFocus()
            } else if (userFirstName.isEmpty()) {
                firstName.error = "Please provide your first name"
                firstName.requestFocus()
            } else if (userLastName.isEmpty()) {
                lastName.error = "Please provide your last name"
                lastName.requestFocus()
            } else if (userPassword.isEmpty()) {
                password.error = "Please provide password"
                password.requestFocus()
            } else if (!passwordAreEquals) {
                repeatPassword.error = "Passwords are not the equals"
            // if no errors
            } else {
                if (!(email.isEmpty() && userPassword.isEmpty())) {
                    // Create user in Firebase
                    mFirebaseAuth!!.createUserWithEmailAndPassword(email, userPassword)
                        .addOnCompleteListener(this) { task ->
                            // if task is not successful
                            if (!task.isSuccessful) {
                                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                                Log.d("Error SignUp", task.exception.toString())
                                // if task is successful
                            } else {
                                // register user in database
                                UserManager.createUser()?.addOnSuccessListener {
                                    // If user created in database, go to main activity
                                    Toast.makeText(this, "Authentication successful", Toast.LENGTH_LONG).show()
                                    val mainActivityIntent = Intent(this, MainActivity::class.java)
                                    startActivity(mainActivityIntent)
                                    finish()
                                }
                            }
                        }
                } else {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setListenerOnSignInLink() {
        signInLink.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}