package com.benlinux.realestatemanager.models

import com.benlinux.realestatemanager.ui.models.User
import org.junit.Assert
import org.junit.Test

class UserUnitTest {

    private val user1 = User("1", "email1@test.com", "first name 1", "last name 1", "avatar url 1")
    private val user2 = User("2", "email2@test.com", "first name 2", "last name 2", "avatar url 2")
    private val user3 = User("3", "email3@test.com", "first name 3", "last name 3", "avatar url 3")


    @Test
    @Throws(Exception::class)
    fun userGetsId() {
        // Test for user id
        Assert.assertEquals("1", user1.id)
        Assert.assertEquals("2", user2.id)
        Assert.assertEquals("3", user3.id)
    }

    @Test
    @Throws(Exception::class)
    fun userGetsEmail() {
        // Test for user email
        Assert.assertEquals("email1@test.com", user1.email)
        Assert.assertEquals("email2@test.com", user2.email)
        Assert.assertEquals("email3@test.com", user3.email)
    }

    @Test
    @Throws(Exception::class)
    fun userGetsFirstName() {
        // Test for user first name
        Assert.assertEquals("first name 1", user1.firstName)
        Assert.assertEquals("first name 2", user2.firstName)
        Assert.assertEquals("first name 3", user3.firstName)
    }

    @Test
    @Throws(Exception::class)
    fun userGetsLastName() {
        // Test for user last name
        Assert.assertEquals("last name 1", user1.lastName)
        Assert.assertEquals("last name 2", user2.lastName)
        Assert.assertEquals("last name 3", user3.lastName)
    }

    @Test
    @Throws(Exception::class)
    fun userGetsAvatar() {
        // Test for user avatar
        Assert.assertEquals("avatar url 1", user1.avatarUrl)
        Assert.assertEquals("avatar url 2", user2.avatarUrl)
        Assert.assertEquals("avatar url 3", user3.avatarUrl)
    }

}