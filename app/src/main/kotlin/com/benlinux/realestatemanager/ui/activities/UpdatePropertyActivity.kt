package com.benlinux.realestatemanager.ui.activities

import android.os.Bundle
import com.benlinux.realestatemanager.R

class UpdatePropertyActivity: AddPropertyActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_proprerty)

        setToolbar()
    }
}