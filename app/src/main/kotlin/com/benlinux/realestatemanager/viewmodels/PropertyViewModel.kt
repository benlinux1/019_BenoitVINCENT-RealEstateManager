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

    // REPOSITORIES
    private val propertyDataSource: PropertyRepository
    private val realtorDataSource : RealtorRepository
    private val executor: Executor

    // DATA
    init {
        this.propertyDataSource = propertyDataSource
        this.realtorDataSource = realtorDataSource
        this.executor = executor
    }

    // GET PROPERTIES LIST
    fun getPropertiesList(): LiveData<MutableList<Property?>> {
        currentProperties = this.propertyDataSource.getAllProperties()
        return currentProperties as LiveData<MutableList<Property?>>
    }


    // CREATE A NEW PROPERTY
    fun createProperty(property: Property) {
        executor.execute { propertyDataSource.createProperty(property) }
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
