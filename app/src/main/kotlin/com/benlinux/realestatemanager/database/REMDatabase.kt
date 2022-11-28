package com.benlinux.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.benlinux.realestatemanager.dao.PropertyDao
import com.benlinux.realestatemanager.dao.RealtorDao
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor
import com.benlinux.realestatemanager.utils.subscribeOnBackground
import java.util.*


@Database(entities = [Property::class], version = 1)
abstract class REMDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao
    abstract fun realtorDao(): RealtorDao

    companion object {
        private var instance: REMDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): REMDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, REMDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        // Create 3 property & 2 realtors in database
        private fun populateDatabase(db: REMDatabase) {
            val propertyDao = db.propertyDao()
            val realtorDao = db.realtorDao()
            subscribeOnBackground {
                propertyDao.insertProperty(Property("1", "Flat", "Marvellous flat", "Paris", 1200000,
                    250, "Marvellous flat in Manhattan with tremendous options...", emptyList(),
                    "12 rue de la Paix - 75000 Paris", true, "28/11/2022", null,
                    Realtor("1", "ben@test.com", "******", "Ben", "Linux")  )
                )
                propertyDao.insertProperty(Property("3", "Duplex", "Fabulous duplex", "London", 2200000,
                    300, "Fabulous Duplex in London with tremendous options...", emptyList(),
                    "10 Downing Street - London SW1A 2AB", true, "28/11/2022", null,
                    Realtor("1", "ben@test.com", "******", "Ben", "Linux") )
                )
                propertyDao.insertProperty(Property("3", "Penthouse", "Exceptional penthouse", "Manhattan", 5200000,
                    300, "Exceptional penthouse in Manhattan with tremendous options...", emptyList(),
                    "66 Perry Street - New York, NY 10014", true, "28/11/2022", null,
                    Realtor("2", "franck@test.com", "******", "Franck", "Black") )
                )
                realtorDao.insertRealtor(Realtor("1", "ben@test.com", "******", "Ben", "Linux") )
                realtorDao.insertRealtor(Realtor("2", "franck@test.com", "******", "Franck", "Black") )
            }
        }
    }
}


