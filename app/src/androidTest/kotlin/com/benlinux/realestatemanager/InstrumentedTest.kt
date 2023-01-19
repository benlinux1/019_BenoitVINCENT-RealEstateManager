package com.benlinux.realestatemanager

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Test


class InstrumentedTest {

    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.benlinux.realestatemanager", appContext.packageName)
    }

}