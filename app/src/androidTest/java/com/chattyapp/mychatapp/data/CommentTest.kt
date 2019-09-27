package com.chattyapp.mychatapp.data

import androidx.test.filters.SmallTest
import org.junit.Assert.*
import org.junit.Test

@SmallTest
class CommentTest {

    @Test
    fun commentShouldHaveEmptyConstructor() {
        val comment = Comment("", User("", "", ""), "")
        assertTrue(comment.toString().isNotEmpty())

    }
}