package com.benlinux.realestatemanager.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.utils.Constants.Companion.CAMERA_ACCESS_PERMISSION
import com.benlinux.realestatemanager.utils.Constants.Companion.IMAGE_CAPTURE_CODE
import com.benlinux.realestatemanager.utils.Constants.Companion.PHOTO_ACCESS_PERMISSION
import com.benlinux.realestatemanager.utils.Constants.Companion.RC_IMAGE_PERMS
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


class SettingsActivity: AppCompatActivity() {

    private lateinit var userAvatar: ImageView
    private lateinit var updateAvatarButton: ImageView
    private lateinit var userFirstName: EditText
    private lateinit var userLastName: EditText
    private lateinit var userEmail: TextView
    private lateinit var deleteButton: Button
    private lateinit var updateButton: Button
    private lateinit var switchRealtor: SwitchCompat

    // For picture remote data
    private var uriImageSelected: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setToolbar()
        setViews()
        updateUIWithUserData()
        setListenerOnUpdateAvatarButton()
        setListenerOnUpdateAccountButton()
        setListenerOnDeleteAccountButton()
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


    // Switch to realtor status
    private fun setListenerOnRealtorSwitch() {
        switchRealtor.setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
            if (checked) {
                showRealtorDialog()
            }
        }
    }


    // When user tries to access to realtor status, show permission dialog
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


    // Retrieve all user data and set it into input fields
    private fun getUserData() {
        fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
        UserManager.getUserData()?.addOnSuccessListener { user ->
            if (user != null) {
                // Set user name
                userFirstName.text = user.firstName!!.toEditable()
                userLastName.text = user.lastName!!.toEditable()
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


    // Set user avatar or coming soon picture into image view according to situation
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


    // When user click on update account button, update all data
    private fun setListenerOnUpdateAccountButton() {
        updateButton.setOnClickListener {
            // update avatar url
            if (uriImageSelected != null) {
                UserManager.updateUserAvatarUrl(uriImageSelected)
                Log.d("Firebase update", "User avatar updated")
            }

            // Input values
            val email = userEmail.text.toString()
            val firstName: String = userFirstName.text.toString()
            val lastName: String = userLastName.text.toString()

            // Check errors
            if (email.isEmpty()) {
                userEmail.error = getString(R.string.please_provide_email)
                userEmail.requestFocus()
            } else if (firstName.isEmpty()) {
                userFirstName.error = getString(R.string.please_provide_firstname)
                userFirstName.requestFocus()
            } else if (lastName.isEmpty()) {
                userLastName.error = getString(R.string.please_provide_lastname)
                userLastName.requestFocus()
            // if no errors
            } else {
                // first name
                UserManager.updateUserFirstName(firstName)
                // last name
                UserManager.updateUserLastName(lastName)
                // realtor status
                UserManager.updateIsRealtor(switchRealtor.isChecked)
                // Update email in Firebase
                val user = UserManager.getCurrentUser()
                user!!.updateEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase update", "User email address updated")
                        // If successful, Update email in Firestore
                        UserManager.updateUserEmailInFirestore(email)
                    }
                }
                Toast.makeText(this, getString(R.string.account_update_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    // When user click on delete account button
    private fun setListenerOnDeleteAccountButton() {
        deleteButton.setOnClickListener {
            // Delete user from firestore
            UserManager.deleteUserFromFirestore().addOnSuccessListener {
                Log.d("Firestore delete", "User account deleted from Firestore")
                // On success delete user from firebase
                UserManager.deleteUser(this).addOnSuccessListener {
                    Log.d("Firebase delete", "User account deleted from Firebase")
                    Toast.makeText(this, getString(R.string.delete_account_succeed), Toast.LENGTH_SHORT).show()
                    navigateToHomeActivity()
                }
            }.addOnFailureListener {
                Toast.makeText(this, getString(R.string.delete_account_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }


    // When click on update avatar button, prompt user to select a media source
    private fun setListenerOnUpdateAvatarButton() {
        updateAvatarButton.setOnClickListener {
            showMediaSelectorDialog()
        }
    }


    // Show dialog to choose between Gallery or Take Photo actions
    private fun showMediaSelectorDialog() {
        // Builder & custom view
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
        val customView = layoutInflater.inflate(R.layout.custom_dialog_media_selector,null)
        builder.setView(customView)
        builder.setCancelable(true)
        val dialogWindow = builder.create()

        // Gallery button
        val galleryButton: ImageView = customView.findViewById(R.id.gallery_button)
        // Camera Button
        val cameraButton: ImageView = customView.findViewById(R.id.camera_button)

        // Gallery button & actions
        galleryButton.setOnClickListener {
            updateAvatarPictureFromGallery()
            dialogWindow.dismiss()
        }

        // Camera button & actions
        cameraButton.setOnClickListener {
            takePhoto()
            dialogWindow.dismiss()
        }

        // Display dialog
        dialogWindow.show()
    }


    // Easy permission result for photo / camera access
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    // When photo access is granted
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private fun updateAvatarPictureFromGallery() {
        // Ask permission (used for API 32 and less)
        if (Build.VERSION.SDK_INT <= 32) {
            if (!EasyPermissions.hasPermissions(this, PHOTO_ACCESS_PERMISSION)) {
                EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.allow_photo_access),
                    RC_IMAGE_PERMS,
                    PHOTO_ACCESS_PERMISSION
                )
                return
            }
        }
        // When permission granted, allow picking action
        Toast.makeText(this, getString(R.string.picture_enabled), Toast.LENGTH_SHORT).show()
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        actionPick.launch(pickPhotoIntent)
    }


    // Create callback when user pick a photo on his device
    private val actionPick = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onPickPhotoResult(result) }


    // Handle result of photo picking activity
    private fun onPickPhotoResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) { //SUCCESS
            assert(result.data != null)
            uriImageSelected = result.data!!.data
            Glide.with(this) //SHOWING PREVIEW OF IMAGE
                .load(uriImageSelected)
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar)
        } else {
            Toast.makeText(this, getString(R.string.no_image_chosen), Toast.LENGTH_SHORT).show()
        }
    }


    // Permission request for Camera and actions
    @AfterPermissionGranted(IMAGE_CAPTURE_CODE)
    private fun takePhoto() {
        // Permission request for camera
        if (!EasyPermissions.hasPermissions(this, CAMERA_ACCESS_PERMISSION)) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.allow_camera_access),
                IMAGE_CAPTURE_CODE,
                CAMERA_ACCESS_PERMISSION
            )
            return

        }
        // When permission granted, allow camera action
        // Content values to get uri with content resolver builder options to write captured image to MediaStore
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Token picture")
        uriImageSelected = this.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        // Create camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Add uri to extras
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImageSelected)
        actionCamera.launch(cameraIntent) // Launch intent
    }


    // Create callback when user take a photo on his device
    private val actionCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> onTakePhotoResult(result) }


    // Handle result of photo capture with device's camera
    private fun onTakePhotoResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) { //SUCCESS
            Glide.with(this) //SHOWING PREVIEW OF IMAGE
                .load(uriImageSelected)
                .apply(RequestOptions.circleCropTransform())
                .into(userAvatar)
        } else {
            Toast.makeText(this, getString(R.string.no_image_chosen), Toast.LENGTH_SHORT).show()
        }
    }

    // Go to main activity
    private fun navigateToHomeActivity() {
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }
}