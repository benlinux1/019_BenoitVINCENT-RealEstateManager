package com.benlinux.realestatemanager.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout

/**
 * Validate text field with regex & set input layout error
 */
fun validateField(field: String, layoutError: TextInputLayout): Boolean {
    val titleRegex = Regex("^[a-zA-Z0-9éèàêîïôö ]*\$")
    return if (field.matches(titleRegex) && field.trim { it <= ' ' }.length > 2) {
        layoutError.error = null
        true
    } else {
        layoutError.error = "This field must contain at least 3 characters"
        false
    }
}

fun validateNumbers(field: String, layoutError: TextInputLayout): Boolean {
    val numberRegex = Regex("^[1-9]+[0-9]*\$")
    return if (field.matches(numberRegex) ) {
        layoutError.error = null
        true
    } else {
        layoutError.error = "This field doesn't seem to contain correct value"
        false
    }
}



/**
 * Set error in layout fields when inpu is empty
 */
fun checkIfFieldsIsNotEmpty(fieldLayout: TextInputLayout): Boolean {
    val errorText = "Please complete this field"
    return if (fieldLayout.editText!!.text.toString() == "") {
        fieldLayout.error = errorText
        false
    } else {
        fieldLayout.error = null
        true
    }
}


fun checkIfPictureIsPresent(picturesList: RecyclerView, picturesError: TextView): Boolean {
    return if (picturesList.toString() == "") {
        picturesError.visibility = View.VISIBLE
        false
    } else {
        picturesError.visibility = View.GONE
        true
    }
}