package com.benlinux.realestatemanager.models

import com.benlinux.realestatemanager.ui.models.PropertyAddress
import org.junit.Assert
import org.junit.Test

class PropertyAddressUnitTest {

    private val address1 = PropertyAddress("number 1", "street 1", "complement 1", "postal code 1", "city 1", "country 1")
    private val address2 = PropertyAddress("number 2", "street 2", "complement 2", "postal code 2", "city 2", "country 2")
    private val address3 = PropertyAddress("number 3", "street 3", "complement 3", "postal code 3", "city 3", "country 3")


    @Test
    @Throws(Exception::class)
    fun addressGetStreetNumber() {
        // Test for address street number
        Assert.assertEquals("number 1", address1.streetNumber)
        Assert.assertEquals("number 2", address2.streetNumber)
        Assert.assertEquals("number 3", address3.streetNumber)
    }

    @Test
    @Throws(Exception::class)
    fun addressGetStreetName() {
        // Test for address street name
        Assert.assertEquals("street 1", address1.streetName)
        Assert.assertEquals("street 2", address2.streetName)
        Assert.assertEquals("street 3", address3.streetName)
    }

    @Test
    @Throws(Exception::class)
    fun addressGetsComplement() {
        // Test for address complement
        Assert.assertEquals("complement 1", address1.complement)
        Assert.assertEquals("complement 2", address2.complement)
        Assert.assertEquals("complement 3", address3.complement)
    }

    @Test
    @Throws(Exception::class)
    fun addressGetsPostalCode() {
        // Test for address street name
        Assert.assertEquals("postal code 1", address1.postalCode)
        Assert.assertEquals("postal code 2", address2.postalCode)
        Assert.assertEquals("postal code 3", address3.postalCode)
    }

    @Test
    @Throws(Exception::class)
    fun addressGetsCity() {
        // Test for address city
        Assert.assertEquals("city 1", address1.city)
        Assert.assertEquals("city 2", address2.city)
        Assert.assertEquals("city 3", address3.city)
    }

    @Test
    @Throws(Exception::class)
    fun addressGetsCountry() {
        // Test for address country
        Assert.assertEquals("country 1", address1.country)
        Assert.assertEquals("country 2", address2.country)
        Assert.assertEquals("country 3", address3.country)
    }
}