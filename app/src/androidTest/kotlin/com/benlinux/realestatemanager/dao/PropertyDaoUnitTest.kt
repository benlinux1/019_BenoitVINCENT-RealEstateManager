package com.benlinux.realestatemanager.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.benlinux.realestatemanager.LiveDataTestUtil
import com.benlinux.realestatemanager.database.REMDatabase
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.*
import java.io.IOException

// Test room database with DAO Interface
class PropertyDaoUnitTest {

    private lateinit var database: REMDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            REMDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun databaseGetsPropertyLiveData() {
        // Define Dao
        val propertyDao = database.propertyDao()
        // Create property
        val picturesList = mutableListOf<Picture?>()
        val address1 = PropertyAddress("number 1", "street 1", "complement 1", "postal code 1", "city 1", "country 1")
        val realtor = User("1", "realtor1@test.com", "first name 1", "last name 1", "avatar url 1")
        val property = Property(1, "flat", "name 1", "area 1", 1000000, 100, "description 1", picturesList, address1, true, "06/01/2023", "", "06/01/2023 08:00:00", realtor, 10, 2, 5)

        // Insert property in room database
        runBlocking(Dispatchers.Default) { propertyDao.insertProperty(property) }

        // Get property by id, and check if data is equal
        Assert.assertEquals(
            LiveDataTestUtil.getValue(propertyDao.getPropertyById(property.id)),
            property
        )
    }
}
