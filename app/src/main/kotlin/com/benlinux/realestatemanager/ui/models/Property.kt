package com.benlinux.realestatemanager.ui.models

import androidx.room.*
import com.benlinux.realestatemanager.utils.converters.Converters
import com.google.firebase.database.IgnoreExtraProperties


@Entity(tableName = "property_table")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@IgnoreExtraProperties
data class Property (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "property_id")
    var id: Int,
    var type: String,
    var name: String,
    var area: String,
    var price: Int,
    var surface: Int,
    var description: String,
    @TypeConverters(Converters::class)
    var pictures: MutableList<Picture?> = mutableListOf(),
    @TypeConverters(Converters::class)
    var address: PropertyAddress,
    var isAvailable: Boolean,
    var creationDate: String,
    var soldDate: String? = null,
    var updateDate: String,
    @TypeConverters(Converters::class)
    var realtor: User,
    var numberOfRooms: Int? = null,
    var numberOfBathrooms: Int? = null,
    var numberOfBedrooms: Int? = null
)
{
    // Constructor used to avoid no-arg constructor & deserialization problems with firebase database
    constructor() : this(
        0,
        "",
        "",
        "",
        0,
        0,
        "",
        mutableListOf(),
        PropertyAddress("", "", "", "", "",""),
        true,
        "",
        null,
        "",
        User("0", "", "", "", "", mutableListOf(), true, mutableListOf())
    )
}

