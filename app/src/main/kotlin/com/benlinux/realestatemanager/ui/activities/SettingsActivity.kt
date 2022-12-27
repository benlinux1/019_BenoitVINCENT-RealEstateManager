package com.benlinux.realestatemanager.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SettingsActivity: AppCompatActivity() {

    private lateinit var userAvatar: ImageView

    private lateinit var userFirstName: EditText
    private lateinit var userLastName: EditText
    private lateinit var userEmail: TextView
    private lateinit var deleteButton: Button
    private lateinit var updateButton: Button
    private lateinit var switchRealtor: SwitchCompat
    private lateinit var updateAvatarButton: ImageView

    // Constants
    annotation class Enum {
        companion object {
            // Permissions for picture picking
            const val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE
            const val RC_IMAGE_PERMS = 100
        }
    }

    private var uriImageSelected: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setToolbar()
        setViews()
        updateUIWithUserData()
    }

    // Toolbar configuration
    private fun setToolbar() {
        val mToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = resources.getString(R.string.settings_title_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    // Define all views
    private fun setViews() {
        userAvatar = findViewById(R.id.settings_user_avatar)
        updateAvatarButton = findViewById(R.id.settings_user_avatar_update)
        userFirstName = findViewById(R.id.settings_user_first_name_field)
        userLastName = findViewById(R.id.settings_user_last_name_field)
        userEmail = findViewById(R.id.settings_user_email_field)
        deleteButton = findViewById(R.id.settings_delete_button)
        updateButton = findViewById(R.id.settings_update_button)
        switchRealtor = findViewById(R.id.settings_realtor_switch)
    }


    // Close settings activity and turn back to main activity if back button is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val mainActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Switch to realtor account creation
    private fun setListenerOnRealtorSwitch() {
        switchRealtor.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
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
                switchRealtor.isChecked = true
                dialogWindow.dismiss()
            } else {
                Log.d("INPUT PASSWORD", password.text.toString())
                switchRealtor.isChecked = false
                password.error = resources.getString(R.string.wrong_password)
            }
        }

        // Negative button & actions
        negativeButton.setOnClickListener {
            switchRealtor.isChecked = false
            dialogWindow.cancel() }

        // Display dialog
        dialogWindow.show()
    }

    // Set User data in fields
    private fun updateUIWithUserData() {
        // If user is logged
        if (UserManager.isCurrentUserLogged()) {
            getUserData()
        }
    }

    private fun getUserData() {
        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
        UserManager.getUserData()?.addOnSuccessListener { user ->
            if (user != null) {
                // Set user name
                userFirstName.text = user.firstName.toEditable()
                userLastName.text = user.lastName.toEditable()
                // User email
                userEmail.text = user.email.toEditable()
                // Set realtor switch
                switchRealtor.isChecked = user.isRealtor
                // Set Avatar picture
                setProfilePicture(user.avatarUrl)
            }
            setListenerOnRealtorSwitch()
        }?.addOnFailureListener { error -> Log.d("USER DATA EXCEPTION", error.toString()) }
    }

    private fun setProfilePicture(profilePictureUrl: String?) {
        if (profilePictureUrl != null) {
            Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar)
        }
        if (profilePictureUrl!!.isEmpty()){
            Glide.with(this)
                .load(R.mipmap.no_photo)
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar)
        }
    }




}