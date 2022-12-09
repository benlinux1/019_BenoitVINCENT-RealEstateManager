package com.benlinux.realestatemanager.utils.converters

import androidx.room.TypeConverter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.Realtor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    // For Pictures
    @TypeConverter
    fun fromList(list : MutableList<Picture>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(data : String) : MutableList<Picture>? {
        val typeToken = object : TypeToken<List<Picture>>() {}.type
        return Gson().fromJson(data,typeToken)
    }


    // For Realtors
    @TypeConverter
    fun realtorToString(realtor: Realtor): String {
        return Gson().toJson(realtor)
    }

    @TypeConverter
    fun stringToRealtor(data: String): Realtor {
        val realtorType = object : TypeToken<Realtor>() {}.type
        return Gson().fromJson(data, realtorType)
    }

    // For Addresses
    @TypeConverter
    fun addressToString(address: PropertyAddress): String {
        return Gson().toJson(address)
    }

    @TypeConverter
    fun stringToAddress(data: String): PropertyAddress {
        val addressType = object : TypeToken<PropertyAddress>() {}.type
        return Gson().fromJson(data, addressType)
    }

}