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
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    return dateFormat.format(Date())
}

@SuppressLint("SimpleDateFormat")
fun convertDateToString(date: Date): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    return dateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertStringToDate(string: String): Date? {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    return formatter.parse(string)
}

@SuppressLint("SimpleDateFormat")
fun convertStringToShortDate(string: String): Date? {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.parse(string)
}