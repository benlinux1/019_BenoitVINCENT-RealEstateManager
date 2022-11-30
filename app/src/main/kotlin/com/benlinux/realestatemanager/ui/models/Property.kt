package com.benlinux.realestatemanager.ui.models

import androidx.room.*

@Entity(tableName = "property_table")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)

data class Property(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "property_id")
    var id: Int = 0,
    var type: String = "",
    var name: String = "",
    var area: String = "",
    var price: Int = 0,
    var surface: Int = 0,
    var description: String = "",
    @Embedded
    @Ignore
    var pictures: List<Picture> = listOf(),
    var address: String = "",
    var isAvailable: Boolean = true,
    var creationDate: String = "29/11/2022",
    var soldDate: String = "",
    @Embedded
    @Ignore
    var realtor: Realtor = Realtor(0, "", "", "", "", ""),
    var numberOfRooms: Int = 0,
    var numberOfBathrooms: Int = 0,
    var numberOfBedrooms: Int = 0
)

