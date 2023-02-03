package com.benlinux.realestatemanager.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.benlinux.realestatemanager.database.REMDatabase
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.addressToString
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.picturesToString
import com.benlinux.realestatemanager.utils.converters.Converters.Companion.realtorToString
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    private lateinit var database: REMDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        database = inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            REMDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    // Test to retrieve a given property values with Content Provider
    fun getFirstPropertyValues() {

        // TEST
        val cursor: Cursor? = mContentResolver!!.query(
            ContentUris.withAppendedId(
                PropertyContentProvider.URI_ITEM,
                PROPERTY_ID
            ), null, null, null, null
        )

        // Position in the list
        Assert.assertEquals(cursor!!.moveToFirst(), (true))

        // Name
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("name")),
            ("Marvellous flat")
        )
        // Area
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("area")),
            ("Paris")
        )

        // Type
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("type")),
            ("Flat")
        )

        // Price
        Assert.assertEquals(
            cursor.getInt(cursor.getColumnIndexOrThrow("price")),
            (1200000)
        )

        // Surface
        Assert.assertEquals(
            cursor.getInt(cursor.getColumnIndexOrThrow("surface")),
            (250)
        )

        // Description
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("description")),
            ("Marvellous flat in Manhattan with tremendous options...")
        )

        // Pictures list
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("pictures")),
            (picturesToString(mutableListOf(
                Picture(
                "https://media.cntraveler.com/photos/60f88f63c28e94c67dd51fbd/master/pass/airbnb%2019839441.jpeg",
                "Lounge")
            )))
        )

        // Address
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("address")),
            addressToString(PropertyAddress("12", "rue de la Paix", "", "75000", "Paris","France"))
        )

        // Availability
        Assert.assertEquals(
            cursor.getBoolean(cursor.getColumnIndexOrThrow("isAvailable")),
            (true)
        )

        // Date of creation
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("creationDate")),
            ("28/11/2022")
        )

        // Date of Sold
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("soldDate")),
            ("")
        )

        // Realtor
        Assert.assertEquals(
            cursor.getString(cursor.getColumnIndexOrThrow("realtor")),
            realtorToString(User("1", "ben@test.com", "Ben", "Linux","", mutableListOf(), false, mutableListOf()))
        )

        // Number of Rooms
        Assert.assertEquals(
            cursor.getInt(cursor.getColumnIndexOrThrow("numberOfRooms")),
            (0)
        )

        // Number of Bedrooms
        Assert.assertEquals(
            cursor.getInt(cursor.getColumnIndexOrThrow("numberOfBedrooms")),
            (0)
        )

        // Number of Bathrooms
        Assert.assertEquals(
            cursor.getInt(cursor.getColumnIndexOrThrow("numberOfBathrooms")),
            (0)
        )
    }

    @Test
    // Test that content provider retrieve the right count in actual database (9)
    fun getPropertiesCount() {

        // Define cursor with query on properties collection
        val cursor: Cursor? = mContentResolver!!.query(
            PropertyContentProvider.URI_COLLECTION, null, null, null, null
        )

        Assert.assertEquals(9,
            cursor!!.count,
        )
    }


    companion object {
        // DATA SET FOR TEST
        private const val PROPERTY_ID: Long = 1

        fun Cursor.getBoolean(columnIndex: Int): Boolean? {
            return if (isNull(columnIndex))
                null
            else
                getInt(columnIndex) != 0
        }
    }
}