package com.benlinux.realestatemanager.utils

import kotlin.math.roundToInt


/**
 * Currency conversion (From Dollar to Euro)
 * @param dollars the original value in dollars
 * @return euro conversion
 */
fun convertDollarToEuro(dollars: Int): Int {
    return (dollars * 0.97566).roundToInt()
}

/**
 * Currency conversion (From Euro to Dollar)
 * @param euros the original value in euro
 * @return dollar conversion
 */
fun convertEuroToDollar(euros: Int): Int {
    return (euros * 1.0247).roundToInt()
}
