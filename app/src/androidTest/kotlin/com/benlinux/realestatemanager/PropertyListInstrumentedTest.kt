package com.benlinux.realestatemanager

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.benlinux.realestatemanager.ui.activities.MainActivity
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class PropertyListInstrumentedTest {


    // Property taken for tests
    private val property = Property(3, "Penthouse", "Exceptional penthouse", "Manhattan", 5200000,
        300, "Exceptional penthouse in Manhattan with tremendous options...", mutableListOf(Picture(
            "https://media.architecturaldigest.com/photos/5c0817ec1b58382d031ba321/2:1/w_4800,h_2400,c_limit/Eighty%20Seven%20Park%20Penthouse%20Family%20Room.jpg",
            "Lounge"),
            Picture("https://pic.le-cdn.com/thumbs/520x390/480/1/properties/Property-62f9abc14ac9ab2b8bf16d26814daef9-125310425.jpg",
                "Exterior"),
            Picture("https://pictures.aqualivingvillas.com/vacation-rentals/golden-beach-house/large/goldenbeachhouse-3.jpg",
                "Pool"),
            Picture("https://www.designferia.com/sites/default/files/images/villa-contemporaine-arcchitecture-vue-mer.jpg",
                "Garden"),
            Picture("https://prestige.excellenceimmobilier.fr/public/img/big/13812059535bed507a7afbb8255167991200jpg5bed9576a362cjpg_5c93bc1784534.jpg",
                "Pool 2")),
        PropertyAddress("66", "Perry Street", "", "NY 10014", "New York", "United States"),
        true, "28/11/2022", "", "12/01/2023 18:43:00",
        User("2eLNnlU4v4VRfZJ8EarjPzOCNi02", "franck@test.com", "Franck", "Black","", mutableListOf(), true, mutableListOf()),0,0,0 )


    @Test
    // We ensure that our recyclerview is displaying at least 1 property
    fun listOfPropertiesShouldNotBeEmptyAndContainsAtLeastNineItems() {
        launchActivity<MainActivity>().use {
            onView(withId(R.id.list_properties))
                .check(matches(hasMinimumChildCount(9)))
        }
    }


    @Test
    // When we click on a property in the list, the property's detailed page is opened
    fun clickOnPropertyInListShouldOpenPropertyDetails() {
        // Click on the third item in the properties list
        launchActivity<MainActivity>().use {
            onView(withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        2,
                        click()
                    )
                )
            // Check if property details page is displayed
            onView(withId(R.id.property_details_main_layout)).check(matches(isDisplayed()))
        }
    }

    // When we click on a property in the list, the data is displayed in the right fields
    @Test
    fun clickOnPropertyShouldDisplayPropertyDataInDetailView() {

        launchActivity<MainActivity>().use {
            // Click on the forth item in the properties list
            onView(withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        2,
                        click()
                    )
                )
            // Check if property data is displayed on the details page
            // Name
            onView(withId(R.id.property_details_information_title)).check(matches(withText(property.name)))
            // Area
            onView(withId(R.id.property_details_information_area)).check(matches(withText(property.area)))
            // Description
            onView(withId(R.id.property_details_description_text)).check(matches(withText(property.description)))
        }
    }


    @Test
    // When we click on ADD BUTTON, the form to add property is displayed
    fun addPropertyButtonShouldOpenAddPropertyForm() {
        launchActivity<MainActivity>().use {

            // LOGIN
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            // Open Login
            onView(withId(R.id.drawer_navigation_login)).perform(click())
            // Check login page is displayed
            onView(withId(R.id.login_layout)).check(matches(isDisplayed()))
            // Enter email
            onView(withId(R.id.login_email)).perform(click())
                .perform(typeText("realtor@test.com"), closeSoftKeyboard())
            // Enter password
            onView(withId(R.id.login_password)).perform(click())
                .perform(typeText("marie1208"), closeSoftKeyboard())
            // Click on login button
            onView(withId(R.id.login_button)).perform(click())

            Thread.sleep(1000)

            // When perform a click on the add button
            onView(withId(R.id.add_property_button)).perform(click())
            // Close keyboard due to autofocus
            onView(withId(R.id.add_name_input)).perform(closeSoftKeyboard())
            // Check if add property form is displayed
            onView(withId(R.id.activity_add_layout)).check(matches(isDisplayed()))

            // Logout
            val backButton = onView(
                Matchers.allOf(
                    withContentDescription("Navigate up"),
                    isDisplayed()
                )
            )
            backButton.perform(click())
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            onView(withId(R.id.drawer_navigation_logout)).perform(click())
        }
    }


    @Test
    // When realtor click on EDIT BUTTON in property details page, the edit form is displayed
    fun editPropertyButtonShouldOpenEditForm() {

        // Click on the forth item in the properties list
        launchActivity<MainActivity>().use {

            // Check if drawer is closed
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            // Open Login
            onView(withId(R.id.drawer_navigation_login)).perform(click())
            // Check login page is displayed
            onView(withId(R.id.login_layout)).check(matches(isDisplayed()))
            // Enter email
            onView(withId(R.id.login_email)).perform(click())
                .perform(typeText("realtor@test.com"), closeSoftKeyboard())
            // Enter password
            onView(withId(R.id.login_password)).perform(click())
                .perform(typeText("marie1208"), closeSoftKeyboard())
            // Click on login button
            onView(withId(R.id.login_button)).perform(click())

            Thread.sleep(1000)
            // Click on third property in the list
            onView(withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        3,
                        click()
                    )
                )
            // Check if property details page is displayed
            onView(withId(R.id.property_details_main_layout)).check(matches(isDisplayed()))
            // When perform a click on the update button
            onView(withId(R.id.property_details_update_button)).perform(click())
            // Close keyboard due to autofocus
            onView(withId(R.id.add_name_input)).perform(closeSoftKeyboard())
            // Check if update property form is displayed
            onView(withId(R.id.activity_add_layout)).check(matches(isDisplayed())) // same layout than add

            // Check if property data (here name, area and price) is displayed in the right fields
            onView(withId(R.id.add_name_input)).check(matches(withText(property.name)))
            onView(withId(R.id.add_area_input)).check(matches(withText(property.area)))
            onView(withId(R.id.add_price_input)).check(matches(withText(property.price.toString())))

            // Logout
            val backButton = onView(
                Matchers.allOf(
                    withContentDescription("Navigate up"),
                    isDisplayed()
                )
            )
            backButton.perform(click())
            backButton.perform(click())
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            onView(withId(R.id.drawer_navigation_logout)).perform(click())
        }
    }





    @Test
    // When we add a property, the item is in the main properties list
    fun addingNewPropertyActionShouldAddItemInMainList() {
        launchActivity<MainActivity>().use {
            // LOGIN
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            // Open Login
            onView(withId(R.id.drawer_navigation_login)).perform(click())
            // Check login page is displayed
            onView(withId(R.id.login_layout)).check(matches(isDisplayed()))
            // Enter email
            onView(withId(R.id.login_email)).perform(click())
                .perform(typeText("realtor@test.com"), closeSoftKeyboard())
            // Enter password
            onView(withId(R.id.login_password)).perform(click())
                .perform(typeText("marie1208"), closeSoftKeyboard())
            // Click on login button
            onView(withId(R.id.login_button)).perform(click())

            Thread.sleep(1000)

            // When perform a click on the add button
            onView(withId(R.id.add_property_button)).perform(click())
            // Close keyboard due to autofocus
            onView(withId(R.id.add_name_input)).perform(closeSoftKeyboard())
            // Check if add property form is displayed
            onView(withId(R.id.activity_add_layout)).check(matches(isDisplayed()))

            // Enter name
            onView(withId(R.id.add_name_input)).perform(click())
                .perform(typeText("NAME"), closeSoftKeyboard())

            // Enter area
            onView(withId(R.id.add_area_input)).perform(click())
                .perform(typeText("area"), closeSoftKeyboard())

            // Enter price
            onView(withId(R.id.add_price_input)).perform(click())
                .perform(typeText(1000000.toString()), closeSoftKeyboard())

            // Enter surface
            onView(withId(R.id.add_surface_input)).perform(click())
                .perform(typeText(1000000.toString()), closeSoftKeyboard())

            // Enter description
            onView(withId(R.id.add_description_text)).perform(click())
                .perform(typeText("description"), closeSoftKeyboard())

            // Enter street number
            onView(withId(R.id.add_street_number_input)).perform(scrollTo(), click())
                .perform(typeText("1"), closeSoftKeyboard())

            // Enter street name
            onView(withId(R.id.add_street_name_input)).perform(scrollTo(), click())
                .perform(typeText("rue de la paix"), closeSoftKeyboard())

            // Enter postal code
            onView(withId(R.id.add_postal_code_input)).perform(scrollTo(), click())
                .perform(typeText("75000"), closeSoftKeyboard())

            // Enter city
            onView(withId(R.id.add_city_input)).perform(typeText("Paris"), closeSoftKeyboard())

            // Enter country
            onView(withId(R.id.add_country_input)).perform(scrollTo(), click())
                .perform(typeText("FRANCE"), closeSoftKeyboard())

            // Click on the creation button to add this new property
            onView(withId(R.id.create))
                .perform(scrollTo(), click())

            // Then : check if item was added to the list
            onView(withId(R.id.list_properties)).check(matches(hasDescendant(withText("NAME"))))
            onView(withId(R.id.list_properties)).check(matches(hasDescendant(withText("area"))))

            // Logout
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            onView(withId(R.id.drawer_navigation_logout)).perform(click())
        }
    }

    @Test
    // When realtor click on EDIT BUTTON in property details page, the edit form is displayed
    fun mainListContainsUpdatedPropertyAfterUpdate() {

        // Launch activity
        launchActivity<MainActivity>().use {

            // LOGIN ----------
            // Check if drawer is closed
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            // Open Login
            onView(withId(R.id.drawer_navigation_login)).perform(click())
            // Check login page is displayed
            onView(withId(R.id.login_layout)).check(matches(isDisplayed()))
            // Enter email
            onView(withId(R.id.login_email)).perform(click())
                .perform(typeText("realtor@test.com"), closeSoftKeyboard())
            // Enter password
            onView(withId(R.id.login_password)).perform(click())
                .perform(typeText("marie1208"), closeSoftKeyboard())
            // Click on login button
            onView(withId(R.id.login_button)).perform(click())

            // LIST DISPLAY ----------
            Thread.sleep(1000)
            // Click on first property in the list
            onView(withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                    )
                )

            // PROPERTY DETAILS DISPLAY ----------
            // Check if property details page is displayed
            onView(withId(R.id.property_details_main_layout)).check(matches(isDisplayed()))
            // When perform a click on the update button
            onView(withId(R.id.property_details_update_button)).perform(click())

            // PROPERTY UPDATE FORM DISPLAY ----------
            // Close keyboard due to autofocus
            onView(withId(R.id.add_name_input)).perform(closeSoftKeyboard())
            // Check if update property form is displayed
            onView(withId(R.id.activity_add_layout)).check(matches(isDisplayed())) // same layout than add

            // Check if property data (here name, area and price) is displayed in the right fields
            onView(withId(R.id.add_name_input)).check(matches(withText("NAME")))

            // Clear name and enter NEW NAME
            onView(withId(R.id.add_name_input)).perform(click()).perform(clearText(), typeText("NEW NAME"), closeSoftKeyboard())
            // Update property
            onView(withId(R.id.create)).perform(scrollTo(), click())

            // LIST DISPLAY ----------
            Thread.sleep(1000)

            // Check if property name has been updated with NEW NAME
            onView(withId(R.id.list_properties)).check(matches(hasDescendant(withText("NEW NAME"))))

            // RESET ORIGINAL NAME
            // Click on first property in the list
            onView(withId(R.id.list_properties))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        0,
                        click()
                    )
                )

            // Check if property details page is displayed
            onView(withId(R.id.property_details_main_layout)).check(matches(isDisplayed()))
            // Perform a click on the update button
            onView(withId(R.id.property_details_update_button)).perform(click())
            // Reset name
            onView(withId(R.id.add_name_input)).perform(click()).perform(clearText(), typeText("NAME"), closeSoftKeyboard())
            // Update property with original name
            onView(withId(R.id.create)).perform(scrollTo(), click())
            // LIST DISPLAY ----------
            Thread.sleep(1000)
            // Check if property name has been updated with NAME
            onView(withId(R.id.list_properties)).check(matches(hasDescendant(withText("NAME"))))

            // LOGOUT
            onView(withId(R.id.activity_main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                // Open drawer menu
                .perform(DrawerActions.open())
            onView(withId(R.id.drawer_navigation_logout)).perform(click())
        }
    }
}