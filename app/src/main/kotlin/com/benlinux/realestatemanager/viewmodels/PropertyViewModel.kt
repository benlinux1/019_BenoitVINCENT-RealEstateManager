package com.benlinux.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benlinux.realestatemanager.repository.PropertyRepository
import com.benlinux.realestatemanager.ui.models.Property
import com.google.android.gms.maps.model.LatLng
import java.util.concurrent.Executor

class PropertyViewModel(propertyDataSource: PropertyRepository, executor: Executor) : ViewModel()  {

    var currentProperties: LiveData<MutableList<Property?>>? = null
    var currentProperty: LiveData<Property>? = null
    var currentPropertyId: Int? = null
    var currentLocation: LiveData<LatLng>? = null


    // REPOSITORIES
    private val propertyDataSource: PropertyRepository
    private val executor: Executor

    // DATA
    init {
        this.propertyDataSource = propertyDataSource
        this.executor = executor
        getPropertiesList()
    }

    // GET PROPERTIES LIST
    private fun getPropertiesList(): LiveData<MutableList<Property?>> {
        currentProperties = this.propertyDataSource.getAllProperties()
        return currentProperties as LiveData<MutableList<Property?>>
    }


    // CREATE A NEW PROPERTY
    fun saveProperty(property: Property) {
        executor.execute { propertyDataSource.createProperty(property) }
    }

    // GET A GIVEN PROPERTY
    fun getPropertyById(propertyId: Int): LiveData<Property> {
        currentPropertyId = propertyId
        currentProperty = this.propertyDataSource.getPropertyById(propertyId)
        executor.execute { propertyDataSource.getPropertyById(propertyId) }
        return propertyDataSource.getPropertyById(propertyId)
    }

    // UPDATE A PROPERTY
    fun updateProperty(property: Property) {
        executor.execute { propertyDataSource.updateProperty(property) }
    }

    // DELETE A GIVEN PROPERTY by id
    fun deletePropertyById(propertyId: Int) {
        executor.execute { propertyDataSource.deletePropertyById(propertyId) }
    }

    // DELETE A GIVEN PROPERTY
    fun deletePropertyById(property: Property) {
        executor.execute { propertyDataSource.deleteProperty(property) }
    }

}
