package com.benlinux.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.ui.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth


class SignupActivity : AppCompatActivity(){

    private lateinit var emailId: EditText
    private lateinit var firstName:EditText
    private lateinit var lastName:EditText
    private lateinit var password:EditText
    private lateinit var switchToRealtor: SwitchCompat
    private lateinit var signUpButton: Button
    private lateinit var signInLink: TextView

    var mFirebaseAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setViews()
        setFirebaseDatabase()
        setListenerOnSignUpButton()
        setListenerOnSignInLink()
        setListenerOnRealtorSwitch()
    }


    private fun setViews() {
        emailId = findViewById(R.id.signup_email)
        firstName = findViewById(R.id.signup_firstName)
        lastName = findViewById(R.id.signup_lastName)
        password = findViewById(R.id.signup_password)
        signUpButton = findViewById(R.id.signup_button)
        signInLink = findViewById(R.id.signup_signIn_link)
        switchToRealtor = findViewById(R.id.signup_realtor_switch)
    }


    private fun setFirebaseDatabase() {
        mFirebaseAuth = FirebaseAuth.getInstance()
    }

    // Sign Up actions
    private fun setListenerOnSignUpButton() {
        signUpButton.setOnClickListener {
            // Define user data
            val email = emailId.text.toString()
            val userFirstName: String = firstName.text.toString()
            val userLastName: String = lastName.text.toString()
            val userPassword: String = password.text.toString()

            val userIsRealtor = switchToRealtor.isChecked

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
                                // Create user object
                                val userToCreate = User(
                                    task.result.user!!.uid, // id
                                    email,
                                    userFirstName,
                                    userLastName,
                                    task.result.user!!.photoUrl.toString(), // avatar
                                    mutableListOf(), // empty favorites list
                                    userIsRealtor, // realtor status
                                    mutableListOf() // empty realtor properties list
                                )

                                // Register user in database
                                UserManager.createUser(userToCreate)?.addOnSuccessListener {
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

    // When user click already gets an account, go to Login Activity
    private fun setListenerOnSignInLink() {
        signInLink.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }

    // Switch to realtor account creation
    private fun setListenerOnRealtorSwitch() {
        switchToRealtor.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
            if (checked) {
                showRealtorDialog()
            }
        }
    }

    // When user tries to create realtor account, show permission dialog
    private fun showRealtorDialog() {
        // Builder & custom view
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val customView = layoutInflater.inflate(R.layout.custom_dialog_realtor_permission,null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Dialog Logo
        val imageView: ImageView = customView.findViewById(R.id.realtor_access_logo)
        // Custom view for password input
        val password: EditText = customView.findViewById(R.id.realtor_password_input)
        password.requestFocus()
        // Negative button
        val negativeButton: Button = customView.findViewById(R.id.realtor_permission_negative_button)
        // Positive button
        val positiveButton: Button = customView.findViewById(R.id.realtor_permission_positive_button)

        // Logo
        Glide.with(this)
            .load(R.drawable.logo)
            .apply(RequestOptions.centerInsideTransform())
            .into(imageView)

        // Positive button & actions
        positiveButton.setOnClickListener {
            if (password.text.toString() == ("realtor123!")) {
                switchToRealtor.isChecked = true
                dialogWindow.dismiss()
            } else {
                Log.d("INPUT PASSWORD", password.text.toString())
                switchToRealtor.isChecked = false
                password.error = resources.getString(R.string.wrong_password)
            }
        }

        // Negative button & actions
        negativeButton.setOnClickListener {
            switchToRealtor.isChecked = false
            dialogWindow.cancel() }

        // Display dialog
        dialogWindow.show()
    }




}