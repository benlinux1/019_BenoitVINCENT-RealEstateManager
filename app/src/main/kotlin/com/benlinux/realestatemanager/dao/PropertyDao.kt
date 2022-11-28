package com.benlinux.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor


/**
 * Sets DAO Pattern Interface to group access to persistent data
 */
@Dao
interface PropertyDao {

    // Insert Property in the table
    @Insert
    fun insertProperty(property: Property)

    // Update Property in the table
    @Update
    fun updateProperty(property: Property)

    // Delete Property in the table
    @Delete
    fun deleteProperty(property: Property)

    // Delete all properties in the table
    @Query("delete from realtor_table")
    fun deleteAllProperties()

    // Deletes a given property in the table
    @Query("DELETE FROM realtor_table WHERE id = :id")
    fun deletePropertyById(id: Int): Int

    // Get all properties from the table
    @Query("select * from realtor_table order by id asc")
    fun getAllProperties(): LiveData<List<Property>>

    // Gets a given property by id
    @Query("SELECT * FROM property_table WHERE id = :id")
    fun getPropertyById(id: Int): LiveData<Property>

}