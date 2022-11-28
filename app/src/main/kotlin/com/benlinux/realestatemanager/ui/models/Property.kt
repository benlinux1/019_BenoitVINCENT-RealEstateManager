package com.benlinux.realestatemanager.ui.models

import com.google.type.DateTime
import com.google.type.LatLng
import kotlin.collections.ArrayList

data class Property(var id: String,
                    var type: String,
                    var name: String,
                    var area: String,
                    var price: Int,
                    var surface: Int = 0,
                    var description: String = "",
                    //var pictures: ArrayList<Picture> =,
                    // var location: LatLng? = LatLng(0, 0),
                    var isAvailable: Boolean,
                    //var creationDate: DateTime,
                    //var soldDate: DateTime?,
                    var realtor: Realtor,
                    //var numberOfRooms: Int?,
                    //var numberOfBathrooms: Int?,
                    //var numberOfBedrooms: Int?
)
