package com.benlinux.realestatemanager.ui.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realtor_table")
data class Realtor(
                    @PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "realtor_id")
                    var id: Int = 0,
                    var email: String = "",
                    var password: String = "",
                    var firstName: String = "",
                    var lastName: String = "",
                    var avatarUrl: String = "" )


