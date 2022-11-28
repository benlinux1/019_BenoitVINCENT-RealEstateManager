package com.benlinux.realestatemanager.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "realtor_table")
data class Realtor(
                    @PrimaryKey(autoGenerate = true)
                    var id: String,
                    var email: String,
                    var password: String,
                    var firstName: String,
                    var lastName: String,
                    var avatarUrl: String? = null )