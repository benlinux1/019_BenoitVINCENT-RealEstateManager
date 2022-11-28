package com.benlinux.realestatemanager.injections

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.benlinux.realestatemanager.database.REMDatabase
import com.benlinux.realestatemanager.repository.PropertyRepository
import com.benlinux.realestatemanager.repository.RealtorRepository
import com.benlinux.realestatemanager.viewmodels.PropertyViewModel
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * ViewModelFactory created to declare ViewModel in MainActivity
 * Used to instantiate correctly PropertyViewModel class
 */
class ViewModelFactory private constructor(context: Context) : ViewModelProvider.Factory {
    private val propertyDataSource: PropertyRepository
    private val realtorDataSource : RealtorRepository
    private val executor: Executor

    init {
        val database: REMDatabase = REMDatabase.getInstance(context)
        propertyDataSource = PropertyRepository(database.propertyDao())
        realtorDataSource = RealtorRepository(database.realtorDao())
        executor = Executors.newSingleThreadExecutor()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java)) {
            return PropertyViewModel(propertyDataSource, realtorDataSource, executor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        private var factory: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory? {
            if (factory == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (factory == null) {
                        factory = ViewModelFactory(context)
                    }
                }
            }
            return factory
        }
    }
}