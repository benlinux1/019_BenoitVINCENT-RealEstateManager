package com.benlinux.realestatemanager.utils

import android.widget.RadioGroup

fun checkIfPropertyTypeIsChecked(mFirstGroup: RadioGroup, mSecondGroup: RadioGroup) {
    val isChecking = booleanArrayOf(true)
    val mCheckedId = IntArray(1)
    mFirstGroup.setOnCheckedChangeListener { _, checkedId ->
        if (checkedId != -1 && isChecking[0]) {
            isChecking[0] = false
            mSecondGroup.clearCheck()
            mCheckedId[0] = checkedId
        }
        isChecking[0] = true
    }
    mSecondGroup.setOnCheckedChangeListener { _, checkedId ->
        if (checkedId != -1 && isChecking[0]) {
            isChecking[0] = false
            mFirstGroup.clearCheck()
            mCheckedId[0] = checkedId
        }
        isChecking[0] = true
    }
}