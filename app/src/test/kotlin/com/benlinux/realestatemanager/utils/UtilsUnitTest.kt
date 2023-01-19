package com.benlinux.realestatemanager.utils

import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class UtilsUnitTest {


    @Test
    @Throws(Exception::class)
    // Euro to dollar conversion
    fun amountInEuroIsConvertedToDollarAndRounded() {
        val euroAmount = 224
        val exchangeRate = 1.0247
        val dollarResult = (euroAmount * exchangeRate).roundToInt()
        Assert.assertEquals(convertEuroToDollar(euroAmount), dollarResult)
    }

    @Test
    @Throws(Exception::class)
    // Dollar to Euro conversion
    fun amountInDollarIsConvertedToEuroAndRounded() {
        val dollarAmount = 2
        val exchangeRate = 1.0247
        val euroResult = (dollarAmount * exchangeRate).roundToInt()
        Assert.assertEquals(convertDollarToEuro(dollarAmount), euroResult)
    }

    @Test
    @Throws(Exception::class)
    // Date format to defined pattern
    fun currentDateIsFormattedToPattern() {
        val today = Date()
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val expectedFormattedDate = dateFormat.format(today)
        Assert.assertEquals(getTodayDate(), expectedFormattedDate)
    }


    @Test
    @Throws(Exception::class)
    // Date to string conversion
    fun dateIsConvertedToString() {
        val calendar = Calendar.getInstance()
        calendar.set(2022, 12, 25)
        val date = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expectedFormattedDate = dateFormat.format(date)
        Assert.assertEquals(convertDateToString(date), expectedFormattedDate)
    }


    @Test
    @Throws(Exception::class)
    // String to date conversion
    fun stringIsFormattedToDate() {
        val stringDate = "13/01/2022 11:48:00"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val expectedFormattedDate = dateFormat.parse(stringDate)
        Assert.assertEquals(convertStringToDate(stringDate), expectedFormattedDate)
    }


    @Test
    @Throws(Exception::class)
    // String to short date conversion
    fun stringIsFormattedToShortDate() {
        val stringDate = "13/01/2022"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val expectedFormattedDate = dateFormat.parse(stringDate)
        Assert.assertEquals(convertStringToShortDate(stringDate), expectedFormattedDate)
    }

}