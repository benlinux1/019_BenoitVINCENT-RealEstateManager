package com.benlinux.realestatemanager.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime
import com.google.type.LatLng
import java.util.*

@Entity(tableName = "property_table")
data class Property(
                    @PrimaryKey(autoGenerate = true)
                    var id: String,
                    var type: String,
                    var name: String,
                    var area: String,
                    var price: Int,
                    var surface: Int = 0,
                    var description: String = "",
                    var pictures: List<Picture> = arrayListOf(),
                    var address: String? = null,
                    var isAvailable: Boolean = true,
                    var creationDate: String = "28/11/2022",
                    var soldDate: Date? = null,
                    var realtor: Realtor? = null,
                    var numberOfRooms: Int? = null,
                    var numberOfBathrooms: Int? = null,
                    var numberOfBedrooms: Int? = null
)
