package com.benlinux.realestatemanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benlinux.realestatemanager.repository.PropertyRepository
import com.benlinux.realestatemanager.repository.RealtorRepository
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor
import java.util.concurrent.Executor

class PropertyViewModel(propertyDataSource: PropertyRepository, realtorDataSource: RealtorRepository, executor: Executor) : ViewModel()  {

    var currentProperties: LiveData<MutableList<Property?>>? = null
    var currentProperty: LiveData<Property>? = null
    var currentPropertyId: Int? = null

    // REPOSITORIES
    private val propertyDataSource: PropertyRepository
    private val realtorDataSource : RealtorRepository
    private val executor: Executor

    // DATA
    init {
        this.propertyDataSource = propertyDataSource
        this.realtorDataSource = realtorDataSource
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


    // Get realtors list
    private fun getRealtorsList(): LiveData<MutableList<Realtor>> {
        return realtorDataSource.getAllRealtors()
    }


    // Get a given Realtor by Id
    fun getRealtorById(id: Int): LiveData<Realtor> {
        return realtorDataSource.getRealtorById(id)
    }
}
