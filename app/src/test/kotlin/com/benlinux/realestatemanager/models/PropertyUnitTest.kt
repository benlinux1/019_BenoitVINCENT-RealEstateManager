package com.benlinux.realestatemanager.models

import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import org.junit.Assert
import org.junit.Test

class PropertyUnitTest {

    private val picturesList = mutableListOf<Picture?>()

    private val address1 = PropertyAddress("number 1", "street 1", "complement 1", "postal code 1", "city 1", "country 1")
    private val address2 = PropertyAddress("number 2", "street 2", "complement 2", "postal code 2", "city 2", "country 2")
    private val address3 = PropertyAddress("number 3", "street 3", "complement 3", "postal code 3", "city 3", "country 3")

    private val realtor = User("1", "realtor1@test.com", "first name 1", "last name 1", "avatar url 1")

    private val property1 = Property(1, "flat", "name 1", "area 1", 1000000, 100, "description 1", picturesList, address1, true, "06/01/2023", "", "06/01/2023 08:00:00", realtor, 10, 2, 5)
    private val property2 = Property(2, "duplex", "name 2", "area 2", 2000000, 200, "description 2", picturesList, address2, false, "16/01/2023", "27/01/2023", "16/01/2023 08:00:00", realtor, 20, 4, 10)
    private val property3 = Property(3, "house", "name 3", "area 3", 3000000, 300, "description 3", picturesList, address3, true, "26/01/2023", "", "26/01/2023 08:00:00", realtor, 40, 16, 20)


    @Test
    @Throws(Exception::class)
    fun propertyGetsId() {
        // Test for property id
        Assert.assertEquals(1, property1.id)
        Assert.assertEquals(2, property2.id)
        Assert.assertEquals(3, property3.id)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsType() {
        // Test for property type
        Assert.assertEquals("flat", property1.type)
        Assert.assertEquals("duplex", property2.type)
        Assert.assertEquals("house", property3.type)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsName() {
        // Test for property name
        Assert.assertEquals("name 1", property1.name)
        Assert.assertEquals("name 2", property2.name)
        Assert.assertEquals("name 3", property3.name)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsArea() {
        // Test for property area
        Assert.assertEquals("area 1", property1.area)
        Assert.assertEquals("area 2", property2.area)
        Assert.assertEquals("area 3", property3.area)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsPrice() {
        // Test for property price
        Assert.assertEquals(1000000, property1.price)
        Assert.assertEquals(2000000, property2.price)
        Assert.assertEquals(3000000, property3.price)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsSurface() {
        // Test for property surface
        Assert.assertEquals(100, property1.surface)
        Assert.assertEquals(200, property2.surface)
        Assert.assertEquals(300, property3.surface)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsDescription() {
        // Test for property area
        Assert.assertEquals("description 1", property1.description)
        Assert.assertEquals("description 2", property2.description)
        Assert.assertEquals("description 3", property3.description)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsPictures() {
        // Test for property pictures
        Assert.assertEquals(picturesList, property1.pictures)
        Assert.assertEquals(picturesList, property2.pictures)
        Assert.assertEquals(picturesList, property3.pictures)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsAddress() {
        // Test for property address
        Assert.assertEquals(address1, property1.address)
        Assert.assertEquals(address2, property2.address)
        Assert.assertEquals(address3, property3.address)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsRealtor() {
        // Test for property realtor
        Assert.assertEquals(realtor, property1.realtor)
        Assert.assertEquals(realtor, property2.realtor)
        Assert.assertEquals(realtor, property3.realtor)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsAvailabilityStatus() {
        // Test for property status
        Assert.assertEquals(true, property1.isAvailable)
        Assert.assertEquals(false, property2.isAvailable)
        Assert.assertEquals(true, property3.isAvailable)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsCreationDate() {
        // Test for property's date of publication
        Assert.assertEquals("06/01/2023", property1.creationDate)
        Assert.assertEquals("16/01/2023", property2.creationDate)
        Assert.assertEquals("26/01/2023", property3.creationDate)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsUpdateDate() {
        // Test for property's date of publication
        Assert.assertEquals("06/01/2023 08:00:00", property1.updateDate)
        Assert.assertEquals("16/01/2023 08:00:00", property2.updateDate)
        Assert.assertEquals("26/01/2023 08:00:00", property3.updateDate)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsSoldDate() {
        // Test for property's date of sold
        Assert.assertEquals("", property1.soldDate)
        Assert.assertEquals("27/01/2023", property2.soldDate)
        Assert.assertEquals("", property3.soldDate)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsNumberOfRooms() {
        // Test for property number of rooms
        Assert.assertEquals(10, property1.numberOfRooms)
        Assert.assertEquals(20, property2.numberOfRooms)
        Assert.assertEquals(40, property3.numberOfRooms)
    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsNumberOfRBathrooms() {
        // Test for property number of bathrooms
        Assert.assertEquals(2, property1.numberOfBathrooms)
        Assert.assertEquals(4, property2.numberOfBathrooms)
        Assert.assertEquals(16, property3.numberOfBathrooms)

    }

    @Test
    @Throws(Exception::class)
    fun propertyGetsNumberOfRBedrooms() {
        // Test for property number of bedrooms
        Assert.assertEquals(5, property1.numberOfBedrooms)
        Assert.assertEquals(10, property2.numberOfBedrooms)
        Assert.assertEquals(20, property3.numberOfBedrooms)

    }
}