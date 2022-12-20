package com.benlinux.realestatemanager.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.benlinux.realestatemanager.utils.converters.Converters

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    var id: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var avatarUrl: String = "",
    @TypeConverters(Converters::class)
    var favorites: MutableList<String> = mutableListOf(),
    var isRealtor: Boolean = false,
    @TypeConverters(Converters::class)
    var realtorProperties: MutableList<String> = mutableListOf() )


