package com.benlinux.realestatemanager

import android.view.Gravity
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.benlinux.realestatemanager.ui.activities.MainActivity
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class OfflineModeUnitTest {


    @Test
    // We ensure that our recyclerview is displaying at least 9 properties in offline mode
    fun listOfPropertiesShouldNotBeEmptyAndContainsAtLeastNineItems() {

        launchActivity<MainActivity>().use {

            // Turn off wifi
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc wifi disable")
            // Turn off mobile data
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("svc data disable")

            // Assert list is visible & contains our 9 items
            onView(ViewMatchers.withId(R.id.list_properties))
                .check(ViewAssertions.matches(ViewMatchers.hasMinimumChildCount(9)))

            // Click on 3rd element in the list
            onView(ViewMatchers.withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        3,
                        ViewActions.click()
                    )
                )

            // Check if property details page is displayed
            onView(ViewMatchers.withId(R.id.property_details_main_layout)).check(
                ViewAssertions.matches(
                    ViewMatchers.isDisplayed()
                )
            )

            // Click back
            val backButton = onView(
                Matchers.allOf(
                    ViewMatchers.withContentDescription("Navigate up"),
                    ViewMatchers.isDisplayed()
                )
            )
            backButton.perform(ViewActions.click())

            // Check we can't connect with realtor account
            // Check if drawer is closed
            onView(ViewMatchers.withId(R.id.activity_main_drawer_layout))
                .check(ViewAssertions.matches(DrawerMatchers.isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            // Open Login
            onView(ViewMatchers.withId(R.id.drawer_navigation_login)).perform(ViewActions.click())
            // Check login page is displayed
            onView(ViewMatchers.withId(R.id.login_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            // Enter email
            onView(ViewMatchers.withId(R.id.login_email)).perform(ViewActions.click())
                .perform(ViewActions.typeText("realtor@test.com"), ViewActions.closeSoftKeyboard())
            // Enter password
            onView(ViewMatchers.withId(R.id.login_password)).perform(ViewActions.click())
                .perform(ViewActions.typeText("marie1208"), ViewActions.closeSoftKeyboard())
            // Click on login button
            onView(ViewMatchers.withId(R.id.login_button)).perform(ViewActions.click())

            // We ensure that user can't login and login activity is still displayed
            onView(ViewMatchers.withId(R.id.login_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            backButton.perform(ViewActions.click())
        }
    }
}