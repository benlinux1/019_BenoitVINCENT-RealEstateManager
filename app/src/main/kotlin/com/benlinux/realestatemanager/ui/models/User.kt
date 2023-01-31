package com.benlinux.realestatemanager.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.benlinux.realestatemanager.utils.converters.Converters

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val id: String,
    var email: String,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatarUrl: String? = null,
    @TypeConverters(Converters::class)
    var favorites: MutableList<String?> = mutableListOf(),
    var isRealtor: Boolean,
    @TypeConverters(Converters::class)
    var realtorProperties: MutableList<String?> = mutableListOf()
)

// Constructor used to avoid no-arg constructor & deserialization problems with firebase database
{
    constructor(): this("", "", "", "", "", mutableListOf(), true, mutableListOf())
}


