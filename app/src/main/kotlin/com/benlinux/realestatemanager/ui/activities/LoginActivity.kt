package com.benlinux.realestatemanager.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.benlinux.realestatemanager.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class LoginActivity : AppCompatActivity() {


    private lateinit var emailId: EditText
    private lateinit var password:EditText
    private lateinit var loginButton: Button
    private lateinit var signUpLink: TextView

    private var firebaseAuth: FirebaseAuth? = null

    private var mAuthStateListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setToolbar()
        setViews()
        setAuthListener()
        setListenerOnLoginButton()
        setListenerOnSignupLink()
    }

    // Define views
    private fun setViews() {
        emailId = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signUpLink = findViewById(R.id.login_signup_link)
    }

    // Toolbar configuration
    private fun setToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = resources.getString(R.string.login_title_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    // Close current activity and turn back to main activity if back button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Redirect user to main activity if already logged
    private fun setAuthListener() {
        firebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = AuthStateListener { firebaseAuth ->
            val mFirebaseUser = firebaseAuth.currentUser
            if (mFirebaseUser != null) {
                navigateToHomeActivity()
            } else {
                Toast.makeText(this, getString(R.string.please_login), Toast.LENGTH_LONG).show()
            }
        }
    }

    // Login button actions with fields validation
    private fun setListenerOnLoginButton() {
        loginButton.setOnClickListener {
            val email = emailId.text.toString()
            val pwd: String = password.text.toString()

            if (email.isEmpty()) {
                emailId.error = getString(R.string.please_provide_email)
                emailId.requestFocus()
            } else if (pwd.isEmpty()) {
                password.error = getString(R.string.please_provide_password)
                password.requestFocus()
            } else if (email.isEmpty() && pwd.isEmpty()) {
                Toast.makeText(this, getString(R.string.fields_empty), Toast.LENGTH_LONG).show()
            } else {
                if (!(email.isEmpty() && pwd.isEmpty())) {
                    firebaseAuth!!.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this) { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this, getString(R.string.error_unknown_error),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(applicationContext, getString(R.string.connection_succeed), Toast.LENGTH_LONG).show()
                                navigateToHomeActivity()
                            }
                        }
                } else {
                    Toast.makeText(this, getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Go to Sign up activity when user clicks on sign up link
    private fun setListenerOnSignupLink() {
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