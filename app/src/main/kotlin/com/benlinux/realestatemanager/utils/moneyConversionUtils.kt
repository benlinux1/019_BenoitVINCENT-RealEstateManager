package com.benlinux.realestatemanager.utils

import kotlin.math.roundToInt


/**
 * Conversion du prix d'un bien immobilier (Dollars vers Euros)
 * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
 * @param dollars
 * @return euro conversion
 */
fun convertDollarToEuro(dollars: Int): Int {
    return (dollars * 0.97566).roundToInt()
}

/**
 * Currency conversion (From Euro to Dollar)
 * @param euros
 * @return dollar conversion
 */
fun convertEuroToDollar(euros: Int): Int {
    return (euros * 1.0247).roundToInt()
}
