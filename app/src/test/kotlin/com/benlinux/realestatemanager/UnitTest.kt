package com.benlinux.realestatemanager

import com.benlinux.realestatemanager.utils.convertDollarToEuro
import com.benlinux.realestatemanager.utils.convertEuroToDollar
import com.benlinux.realestatemanager.utils.getTodayDate
import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class UnitTest {


    @Test
    @Throws(Exception::class)
    // Euro to dollar conversion
    fun amountInEuroIsConvertedToDollarAndRounded() {
        val euroAmount1 = 2
        val euroAmount2 = 224
        val exchangeRate = 1.0247
        val euroResult1 = (euroAmount1 * exchangeRate).roundToInt()
        val euroResult2 = (euroAmount2 * exchangeRate).roundToInt()
        Assert.assertEquals(convertDollarToEuro(euroAmount1), euroResult1)
        Assert.assertEquals(convertEuroToDollar(euroAmount2), euroResult2)
    }

    @Test
    @Throws(Exception::class)
    // Date format to defined pattern
    fun currentDateIsFormattedToPattern() {
        val today = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expectedFormattedDate = dateFormat.format(today)
        Assert.assertEquals(getTodayDate(), expectedFormattedDate)
    }
}