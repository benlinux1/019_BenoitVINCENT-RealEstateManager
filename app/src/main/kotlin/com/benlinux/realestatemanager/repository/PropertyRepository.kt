package com.benlinux.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.benlinux.realestatemanager.dao.PropertyDao
import com.benlinux.realestatemanager.ui.models.Property


class PropertyRepository(propertyDao: PropertyDao) {

    private val propertyDao: PropertyDao

    init {
        this.propertyDao = propertyDao
    }

    // --- GET PROPERTIES LIST---
    fun getAllProperties(): LiveData<MutableList<Property?>> {
        return propertyDao.getAllProperties()
    }

    // --- GET ONE PROPERTY BY ID---
    fun getPropertyById(propertyId: Int): LiveData<Property> {
        return propertyDao.getPropertyById(propertyId)
    }

    // --- CREATE NEW PROPERTY  ---
    fun createProperty(property: Property) {
        propertyDao.insertProperty(property)
    }

    // --- DELETE PROPERTY BY ID ---
    fun deletePropertyById(propertyId: Int) {
        propertyDao.deletePropertyById(propertyId)
    }

    // --- DELETE PROPERTY ---
    fun deleteProperty(property: Property) {
        propertyDao.deleteProperty(property)
    }

    // --- UPDATE PROPERTY ---
    fun updateProperty(property: Property) {
        propertyDao.updateProperty(property)
    }
}