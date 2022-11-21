package com.benlinux.realestatemanager.ui.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.utils.convertDollarToEuro
import com.benlinux.realestatemanager.utils.isInternetAvailable


class MainActivity: AppCompatActivity() {

    private lateinit var textViewMain: TextView
    private lateinit var textViewQuantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
        checkInternetConnection()
        configureTextViewMain()
        configureTextViewQuantity()
    }

    private fun setViews() {
        // Update activity layout name for textView, from second to main
        // this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
    }

    private fun checkInternetConnection() {
        isInternetAvailable(applicationContext)
    }

    private fun configureTextViewMain() {
        textViewMain.textSize = 15f
        textViewMain.text = applicationContext.resources.getString(R.string.main_activity_message)
    }

    private fun configureTextViewQuantity() {
        // int quantity = Utils.convertDollarToEuro(100);   Must be a string to be readable
        val quantity = convertDollarToEuro(100).toString() + "€"
        textViewQuantity.textSize = 20f
        textViewQuantity.text = quantity
    }

}