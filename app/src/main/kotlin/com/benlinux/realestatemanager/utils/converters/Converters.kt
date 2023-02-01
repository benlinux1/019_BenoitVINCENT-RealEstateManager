package com.benlinux.realestatemanager.utils.converters

import androidx.room.TypeConverter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
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
    fun realtorToString(user: User): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun stringToRealtor(data: String): User {
        val userType = object : TypeToken<User>() {}.type
        return Gson().fromJson(data, userType)
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

    // For user favorites
    @TypeConverter
    fun fromStringList(list : MutableList<String>?): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(data : String) : MutableList<String>? {
        val stringType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, stringType)
    }

    companion object {

        fun stringToRealtor(data: String): User {
            val userType = object : TypeToken<User>() {}.type
            return Gson().fromJson(data, userType)
        }

        fun realtorToString(user: User): String {
            return Gson().toJson(user)
        }

        fun stringToPicturesList(data : String) : MutableList<Picture?> {
            val typeToken = object : TypeToken<List<Picture>>() {}.type
            return Gson().fromJson(data, typeToken)
        }

        fun stringToAddress(data: String): PropertyAddress {
            val addressType = object : TypeToken<PropertyAddress>() {}.type
            return Gson().fromJson(data, addressType)
        }

        fun addressToString(address: PropertyAddress): String {
            return Gson().toJson(address)
        }

        fun picturesToString(list : MutableList<Picture?>): String {
            return Gson().toJson(list)
        }
    }

}