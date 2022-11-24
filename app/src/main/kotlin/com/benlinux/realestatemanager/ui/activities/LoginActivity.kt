package com.benlinux.realestatemanager.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.benlinux.realestatemanager.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes.NO_NETWORK
import com.firebase.ui.auth.ErrorCodes.UNKNOWN_ERROR
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //checkIfUserIsConnected(applicationContext)
    }

    // Create callback for Firebase authentication result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    // Firebase SignIn
    private fun startSignInActivity() {
        // Choose authentication providers
        val providers: List<AuthUI.IdpConfig> =arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(signInIntent)
    }

    // Handler for response after SignIn Activity close
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in



        } else {
            // ERRORS
            if (response == null) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_authentication_canceled),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (response.error != null) {
                if (response.error!!.equals(NO_NETWORK))
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
            } else if (response.error!!.equals(UNKNOWN_ERROR)) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_unknown_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_unknown_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    /**
    // Check user status & redirect to MainActivity if logged
    private fun checkIfUserIsConnected(context: Context) {
        // TODO if user is logged {
            startMainActivity(context)
        // TODO } else {
            startSignInActivity()
        //}
    }
    */

    // Launch Main Activity
    private fun startMainActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }

}