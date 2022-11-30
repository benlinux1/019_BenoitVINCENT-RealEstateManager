package com.benlinux.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.benlinux.realestatemanager.ui.models.Realtor


/**
 * Sets DAO Pattern Interface to group access to persistent data
 */
@Dao
interface RealtorDao {

    // Insert Realtor in the table
    @Insert
    fun insertRealtor(realtor: Realtor)

    // Update Realtor in the table
    @Update
    fun updateRealtor(realtor: Realtor)

    @Delete
    fun deleteRealtor(realtor: Realtor)

    @Query("delete from realtor_table")
    fun deleteAllRealtors()

    // Deletes a given realtor in the table
    @Query("DELETE FROM realtor_table WHERE realtor_id = :id")
    fun deleteRealtorById(id: Int): Int

    @Query("select * from realtor_table order by realtor_id asc")
    fun getAllRealtors(): LiveData<List<Realtor>>

    // Gets a given realtor by id
    @Query("SELECT * FROM realtor_table WHERE realtor_id = :id")
    fun getRealtorById(id: Int): LiveData<Realtor>

}