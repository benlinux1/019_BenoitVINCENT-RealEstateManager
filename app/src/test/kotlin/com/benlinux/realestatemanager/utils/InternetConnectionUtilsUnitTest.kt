package com.benlinux.realestatemanager.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowNetworkInfo

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
@Suppress("DEPRECATION")
class InternetConnectionUtilsUnitTest {

    private lateinit var connectivityManager: ConnectivityManager
    private var activeNetworkInfo: ShadowNetworkInfo? = null
    private lateinit var context: Context


    @Before
    // Set up context and connectivity manager before tests
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        activeNetworkInfo = Shadows.shadowOf(connectivityManager.activeNetworkInfo)
    }

    @Test
    @Throws(Exception::class)
    // When internet is CONNECTED -> internet is available
    fun connectedNetworkShouldReturnInternetIsAvailable() {
        activeNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTED)
        Assert.assertTrue(isInternetAvailable(ApplicationProvider.getApplicationContext()))
    }

    @Test
    @Throws(Exception::class)
    // When internet is CONNECTING -> internet is not available
    fun connectingNetworkShouldReturnInternetIsNotAvailable() {
        activeNetworkInfo!!.setConnectionStatus(NetworkInfo.State.CONNECTING)
        Assert.assertFalse(isInternetAvailable(ApplicationProvider.getApplicationContext()))
    }

    @Test
    @Throws(Exception::class)
    // When internet is disconnected -> internet is not available
    fun disconnectedNetworkShouldReturnInternetIsNotAvailable() {
        activeNetworkInfo!!.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        Assert.assertFalse(isInternetAvailable(ApplicationProvider.getApplicationContext()))
    }
}