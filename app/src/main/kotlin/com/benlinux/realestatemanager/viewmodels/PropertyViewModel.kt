package com.benlinux.realestatemanager.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.benlinux.realestatemanager.data.propertyManager.PropertyManager
import com.benlinux.realestatemanager.data.userManager.UserManager
import com.benlinux.realestatemanager.repository.PropertyRepository
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.utils.convertStringToDate
import java.util.*
import java.util.concurrent.Executor

class PropertyViewModel(propertyDataSource: PropertyRepository, executor: Executor) : ViewModel() {

    var currentProperties: LiveData<MutableList<Property?>>? = null
    var currentProperty: LiveData<Property>? = null
    private var currentPropertyId: Int? = null

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

    // Synchronize properties data with local Room database & remote Firestore Database
    fun syncLocalAndRemoteDatabases() {
        // Call remote firestore properties data
        PropertyManager.getAllPropertiesData().addOnCompleteListener { task ->
            val firebaseList = task.result
            if (firebaseList != currentProperties?.value) {
                // Check if remote properties are on local database
                for (firebaseProperty in firebaseList!!) {
                    // If remote property is not in Room Database
                    if (currentProperties?.value != null) {
                        if (!currentProperties?.value!!.contains(firebaseProperty)) {
                            // If property is really absent, save it in local Room Database, and update realtor account
                            if (firebaseProperty != null && !currentProperties?.value!!.any { it!!.id == firebaseProperty.id }) {
                                saveProperty(firebaseProperty)
                                UserManager.addPropertyToRealtorProperties(firebaseProperty.id.toString())
                                // else, it means that property has been updated
                            } else {
                                // check date of update
                                for (localProperty in currentProperties?.value!!) {
                                    val localPropertyDateOfUpdate: Date? = if (localProperty!!.updateDate.isNotEmpty()) {
                                        convertStringToDate(localProperty.updateDate)
                                    } else {
                                        Date(0)
                                    }
                                    val firebasePropertyDateOfUpdate: Date? =  if (firebaseProperty!!.updateDate.isNotEmpty()) {
                                        convertStringToDate(firebaseProperty.updateDate)
                                    } else {
                                        Date(0)
                                    }

                                    if (localProperty.id == firebaseProperty.id) {
                                        // Update property in local or remote database according to last update
                                        if (localPropertyDateOfUpdate!!.before(firebasePropertyDateOfUpdate) ) {
                                            updateProperty(firebaseProperty)
                                        } else {
                                            PropertyManager.updateProperty(localProperty).addOnCompleteListener {
                                                Log.d("FIREBASE UPDATE", "Room property imported")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Check if local properties are on Firebase
                if (currentProperties?.value != null) {
                    for (localProperty in currentProperties?.value!!) {
                        // If local property is not on Firebase, save it
                        if (!firebaseList.contains(localProperty)) {
                            // If property is really absent, save it in Firebase database, and update realtor account
                            if (localProperty != null && !firebaseList.any { it!!.id == localProperty.id }) {
                                // if realtor created property without internet connexion, update also realtor data
                                if (localProperty.realtor.email.isEmpty()) {
                                    UserManager.getUserDataById(localProperty.realtor.id)
                                        .addOnSuccessListener { user ->
                                            localProperty.realtor = user!!
                                            updateProperty(localProperty)
                                            PropertyManager.createProperty(localProperty)
                                            UserManager.addPropertyToRealtorProperties(localProperty.id.toString())
                                        }
                                    // If realtor is already stored, only save property in firebase
                                } else {
                                    PropertyManager.createProperty(localProperty)
                                    UserManager.addPropertyToRealtorProperties(localProperty.id.toString())
                                }
                                // Else, it means that property was updated
                            } else {
                                // check date of update
                                for (firebaseProperty in firebaseList) {
                                    val localPropertyDateOfUpdate: Date? = if (localProperty!!.updateDate.isNotEmpty()) {
                                        convertStringToDate(localProperty.updateDate)
                                    } else {
                                        Date(0)
                                    }
                                    val firebasePropertyDateOfUpdate: Date? =  if (firebaseProperty!!.updateDate.isNotEmpty()) {
                                        convertStringToDate(firebaseProperty.updateDate)
                                    } else {
                                        Date(0)
                                    }
                                    if (localProperty.id == firebaseProperty.id) {
                                        // Update property in local or remote database according to last update
                                        if (localPropertyDateOfUpdate!!.before(firebasePropertyDateOfUpdate) ) {
                                            updateProperty(firebaseProperty)
                                        } else {
                                            PropertyManager.updateProperty(localProperty).addOnCompleteListener {
                                                Log.d("FIREBASE UPDATE", "Room property imported")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
