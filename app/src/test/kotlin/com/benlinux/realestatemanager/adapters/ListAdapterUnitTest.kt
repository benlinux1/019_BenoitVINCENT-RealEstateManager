package com.benlinux.realestatemanager.adapters

import android.content.Context
import com.benlinux.realestatemanager.ui.adapters.ListAdapter
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ListAdapterUnitTest {

    // Create empty properties list
    private val propertiesList: MutableList<Property?> = mutableListOf()

    // Create empty pic list
    private val picturesList: MutableList<Picture?> = mutableListOf()

    // Mock context
    private val context: Context = Mockito.mock(Context::class.java)

    // Create 3 addresses
    private val address1 = PropertyAddress("number 1", "street 1", "complement 1", "postal code 1", "city 1", "country 1")
    private val address2 = PropertyAddress("number 2", "street 2", "complement 2", "postal code 2", "city 2", "country 2")
    private val address3 = PropertyAddress("number 3", "street 3", "complement 3", "postal code 3", "city 3", "country 3")

    // Generate 1 realtor
    private val realtor = User("1", "realtor1@test.com", "first name 1", "last name 1", "avatar url 1")

    // Create 3 properties
    private val property1 = Property(1, "flat", "name 1", "area 1", 1000000, 100, "description 1", picturesList, address1, true, "06/01/2023", "", "06/01/2023 08:00:00", realtor, 10, 2, 5)
    private val property2 = Property(2, "duplex", "name 2", "area 2", 2000000, 200, "description 2", picturesList, address2, false, "16/01/2023", "27/01/2023", "16/01/2023 08:00:00", realtor, 20, 4, 10)
    private val property3 = Property(3, "house", "name 3", "area 3", 3000000, 300, "description 3", picturesList, address3, true, "26/01/2023", "", "26/01/2023 08:00:00", realtor, 40, 16, 20)

    // Define properties list adapter
    private val propertyAdapter = ListAdapter(propertiesList, context)

    @Before
    // Init properties list before each test
    fun initList() {
        // Add this 3 properties to properties list
        propertiesList.add(property1)
        propertiesList.add(property2)
        propertiesList.add(property3)
    }


    @Test
    @Throws(Exception::class)
    // Check data in adapter
    fun propertiesAdapterGetsPictureData() {
        // Check properties list size
        Assert.assertEquals(propertiesList.size.toLong(), 3)

        // Check items in adapter
        Assert.assertEquals(propertyAdapter.getItem(0), property1)
        Assert.assertEquals(propertyAdapter.getItem(1), property2)
        Assert.assertEquals(propertyAdapter.getItem(2), property3)
    }

    @Test
    @Throws(Exception::class)
    // Check title in property adapter
    fun propertyAdapterGetsTitle() {
        Assert.assertEquals(propertyAdapter.getItem(0)?.name, property1.name)
        Assert.assertEquals(propertyAdapter.getItem(1)?.name, property2.name)
        Assert.assertEquals(propertyAdapter.getItem(2)?.name, property3.name)
    }

    @Test
    @Throws(Exception::class)
    // Check area in property adapter
    fun propertyAdapterGetsArea() {
        Assert.assertEquals(propertyAdapter.getItem(0)?.area, property1.area)
        Assert.assertEquals(propertyAdapter.getItem(1)?.area, property2.area)
        Assert.assertEquals(propertyAdapter.getItem(2)?.area, property3.area)
    }

    @Test
    @Throws(Exception::class)
    // Check price in property adapter
    fun propertyAdapterGetsPrice() {
        Assert.assertEquals(propertyAdapter.getItem(0)?.price, property1.price)
        Assert.assertEquals(propertyAdapter.getItem(1)?.price, property2.price)
        Assert.assertEquals(propertyAdapter.getItem(2)?.price, property3.price)
    }

    @Test
    @Throws(Exception::class)
    // Check picture in property adapter
    fun propertyAdapterGetsPicture() {
        Assert.assertEquals(propertyAdapter.getItem(0)?.pictures, property1.pictures)
        Assert.assertEquals(propertyAdapter.getItem(1)?.pictures, property2.pictures)
        Assert.assertEquals(propertyAdapter.getItem(2)?.pictures, property3.pictures)
    }
}