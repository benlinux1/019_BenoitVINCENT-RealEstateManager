package com.benlinux.realestatemanager.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Conversion de la date d'aujourd'hui en un format plus approprié
 * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
 * @return
 */
@SuppressLint("SimpleDateFormat")
fun getTodayDate(): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(date)
}
