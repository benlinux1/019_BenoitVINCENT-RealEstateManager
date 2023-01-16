package com.benlinux.realestatemanager.models

import com.benlinux.realestatemanager.ui.models.Picture
import org.junit.Assert
import org.junit.Test


class PictureUnitTest {

    private val picture1 = Picture("https://www.url1.com", "room1")
    private val picture2 = Picture("https://www.url2.com", "room2")
    private val picture3 = Picture("https://www.url3.com", "room3")


    @Test
    @Throws(Exception::class)
    fun pictureGetUrl() {
        // Test for picture Url
        Assert.assertEquals("https://www.url1.com", picture1.url)
        Assert.assertEquals("https://www.url2.com", picture2.url)
        Assert.assertEquals("https://www.url3.com", picture3.url)
    }

    @Test
    @Throws(Exception::class)
    fun pictureGetRoom() {
        // Test for picture room
        Assert.assertEquals("room1", picture1.room)
        Assert.assertEquals("room2", picture2.room)
        Assert.assertEquals("room3", picture3.room)
    }

}