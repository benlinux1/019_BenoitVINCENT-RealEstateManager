package com.benlinux.realestatemanager.utils

import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun addressToString(address: PropertyAddress): String {
    return Gson().toJson(address)
}

fun stringToAddress(data: String): PropertyAddress {
    val addressType = object : TypeToken<PropertyAddress>() {}.type
    return Gson().fromJson(data, addressType)
}