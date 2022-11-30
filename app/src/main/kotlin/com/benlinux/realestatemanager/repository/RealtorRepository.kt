package com.benlinux.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.benlinux.realestatemanager.dao.RealtorDao
import com.benlinux.realestatemanager.ui.models.Realtor

class RealtorRepository(realtorDao: RealtorDao) {

    private val realtorDao: RealtorDao

    init {
        this.realtorDao = realtorDao
    }

    // --- GET PROPERTIES LIST---
    fun getAllRealtors(): LiveData<MutableList<Realtor>> {
        return realtorDao.getAllRealtors()
    }

    // --- GET ONE PROPERTY BY ID---
    fun getRealtorById(realtorId: Int): LiveData<Realtor> {
        return realtorDao.getRealtorById(realtorId)
    }

    // --- CREATE NEW PROPERTY  ---
    fun createRealtor(realtor: Realtor) {
        realtorDao.insertRealtor(realtor)
    }

    // --- DELETE PROPERTY---
    fun deleteRealtor(realtorId: Int) {
        realtorDao.deleteRealtorById(realtorId)
    }

    // --- UPDATE PROPERTY---
    fun updateRealtor(realtor: Realtor) {
        realtorDao.updateRealtor(realtor)
    }
}