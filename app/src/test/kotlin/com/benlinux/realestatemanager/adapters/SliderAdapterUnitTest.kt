package com.benlinux.realestatemanager.adapters

import android.content.Context
import com.benlinux.realestatemanager.ui.adapters.SliderAdapter
import com.benlinux.realestatemanager.ui.models.Picture
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SliderAdapterUnitTest {

    // Create empty pictures list
    private val picturesList: MutableList<Picture?> = mutableListOf()

    // Mock context
    private val context: Context = Mockito.mock(Context::class.java)

    // Define 3 pictures
    private val picture1 = Picture("https://www.url1.com", "room1")
    private val picture2 = Picture("https://www.url2.com", "room2")
    private val picture3 = Picture("https://www.url3.com", "room3")

    // Define slider adapter
    private val sliderAdapter = SliderAdapter(picturesList, context)

    @Before
    // Init pictures list before each test
    fun initList() {
        // Add this 3 pictures to pictures list
        picturesList.add(picture1)
        picturesList.add(picture2)
        picturesList.add(picture3)
    }


    @Test
    @Throws(Exception::class)
    // Check data in adapter
    fun sliderAdapterGetsPictureData() {
        // Check pictures list size
        Assert.assertEquals(picturesList.size.toLong(), 3)

        // Check items in adapter
        Assert.assertEquals(sliderAdapter.getItem(0), picture1)
        Assert.assertEquals(sliderAdapter.getItem(1), picture2)
        Assert.assertEquals(sliderAdapter.getItem(2), picture3)
    }

    @Test
    @Throws(Exception::class)
    // Check picture url in slider adapter
    fun sliderAdapterGetsUrl() {
        Assert.assertEquals(sliderAdapter.getItem(0)?.url, picture1.url)
        Assert.assertEquals(sliderAdapter.getItem(1)?.url, picture2.url)
        Assert.assertEquals(sliderAdapter.getItem(2)?.url, picture3.url)
    }

    @Test
    @Throws(Exception::class)
    // Check picture room name in slider adapter
    fun sliderAdapterGetsRoom() {
        Assert.assertEquals(sliderAdapter.getItem(0)?.room, picture1.room)
        Assert.assertEquals(sliderAdapter.getItem(1)?.room, picture2.room)
        Assert.assertEquals(sliderAdapter.getItem(2)?.room, picture3.room)
    }
}