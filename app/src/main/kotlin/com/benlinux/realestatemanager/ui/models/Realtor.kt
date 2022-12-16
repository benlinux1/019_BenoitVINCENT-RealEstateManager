package com.benlinux.realestatemanager.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realtor_table")
data class Realtor(
    @PrimaryKey
    @ColumnInfo(name = "realtor_id")
    var id: String = "",
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var avatarUrl: String = "" )


