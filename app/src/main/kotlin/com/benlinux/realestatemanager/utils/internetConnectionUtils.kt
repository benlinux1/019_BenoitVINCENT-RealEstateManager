package com.benlinux.realestatemanager.utils

import android.content.Context
import android.net.wifi.WifiManager


/**
 * Vérification de la connexion réseau
 * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
 * @param context
 * @return
 */
fun isInternetAvailable(context: Context): Boolean {
    val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    return wifi.isWifiEnabled
}
