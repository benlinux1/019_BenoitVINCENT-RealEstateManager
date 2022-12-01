package com.benlinux.realestatemanager.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benlinux.realestatemanager.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Validate text field with regex & set input layout error
 */
fun validateField(field: String, layoutError: TextInputLayout): Boolean {
    val titleRegex = Regex("^[a-zA-Z0-9éèàêîïôö ]*$")
    return if (field.matches(titleRegex) && field.trim { it <= ' ' }.length > 2) {
        layoutError.error = null
        true
    } else {
        layoutError.error = R.string.length_error.toString()
        false
    }
}

fun validateNumbers(field: String, layoutError: TextInputLayout): Boolean {
    val titleRegex = Regex("^[0-9]*$")
    return if (field.matches(titleRegex) && field.trim { it <= ' ' }.length > 2) {
        layoutError.error = null
        true
    } else {
        layoutError.error = R.string.numbers_error.toString()
        false
    }
}


/**
 * Set error in layout fields when inpu is empty
 */
fun checkIfFieldsAreNotEmpty(
    areaLayout: TextInputLayout,
    priceLayout: TextInputLayout,
    surfaceLayout: TextInputLayout,
    descriptionLayout: TextInputLayout,
    picturesList: RecyclerView,
    picturesError: TextView
): Boolean {
    if (areaLayout.editText!!.text.toString() == "") {
        areaLayout.error = R.string.empty_error.toString()
    } else if (priceLayout.editText!!.text.toString() == "") {
        priceLayout.error = R.string.empty_error.toString()
    } else if (surfaceLayout.editText!!.text.toString() == "") {
        surfaceLayout.error = R.string.empty_error.toString()
    } else if (descriptionLayout.editText!!.text.toString() == "") {
        descriptionLayout.error = R.string.empty_error.toString()
    } else if (picturesList.toString() == "") {
        picturesError.visibility = View.VISIBLE
    } else {
        return true
    }
    return false
}