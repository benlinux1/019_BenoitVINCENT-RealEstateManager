package com.benlinux.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.benlinux.realestatemanager.R
import com.benlinux.realestatemanager.utils.checkIfPropertyTypeIsChecked

class AddPropertyFragment: Fragment() {

    private lateinit var fragmentView: View
    private lateinit var typeRadioGroup1: RadioGroup
    private lateinit var typeRadioGroup2: RadioGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentView = inflater.inflate(R.layout.fragment_add_property_view, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRadioButtons()
    }

    private fun setRadioButtons() {
        typeRadioGroup1 = fragmentView.findViewById(R.id.add_type_radioGroup1)
        typeRadioGroup2 = fragmentView.findViewById(R.id.add_type_radioGroup2)
        checkIfPropertyTypeIsChecked(typeRadioGroup1, typeRadioGroup2)
    }

}

