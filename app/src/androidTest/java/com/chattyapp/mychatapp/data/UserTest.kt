package com.chattyapp.mychatapp.data

import androidx.test.filters.SmallTest
import org.junit.Assert.*
import org.junit.Test

@SmallTest
class UserTest {

    @Test
    fun userShouldHaveAnEmptyConstructor() {
        val user = User("", "", "")
        assertTrue(user.toString().isNotEmpty())

    }
}