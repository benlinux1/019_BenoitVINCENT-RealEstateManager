package com.benlinux.realestatemanager.provider

import android.content.ContentValues
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.stringToAddress
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.stringToPicturesList
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.stringToRealtor


fun propertyFromContentValues(values: ContentValues): Property {
    val property = Property()
    if(values.containsKey("property_id")) property.id = values.getAsInteger("property_id")
    if(values.containsKey("type")) property.type = values.getAsString("type")
    if(values.containsKey("name")) property.name = values.getAsString("name")
    if(values.containsKey("area")) property.area = values.getAsString("area")
    if(values.containsKey("price")) property.price = values.getAsInteger("price")
    if(values.containsKey("surface")) property.surface = values.getAsInteger("surface")
    if(values.containsKey("description")) property.description = values.getAsString("description")
    if(values.containsKey("pictures")) property.pictures = stringToPicturesList(values.getAsString("pictures"))
    if(values.containsKey("address")) property.address = stringToAddress(values.getAsString("address"))
    if(values.containsKey("isAvailable")) property.isAvailable = values.getAsBoolean("isAvailable")
    if(values.containsKey("creationDate")) property.creationDate = values.getAsString("creationDate")
    if(values.containsKey("updateDate")) property.updateDate = values.getAsString("updateDate")
    if(values.containsKey("soldDate")) property.soldDate = values.getAsString("soldDate")
    if(values.containsKey("realtor")) property.realtor = stringToRealtor(values.getAsString("realtor"))
    if(values.containsKey("numberOfRooms")) property.numberOfRooms = values.getAsInteger("numberOfRooms")
    if(values.containsKey("numberOfBedrooms")) property.numberOfBedrooms = values.getAsInteger("numberOfBedrooms")
    if(values.containsKey("numberOfBathrooms")) property.numberOfBathrooms = values.getAsInteger("numberOfBathrooms")

    return property
}