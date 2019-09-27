package com.chattyapp.mychatapp.data

import androidx.test.filters.SmallTest
import org.junit.Test

import org.junit.Assert.*

@SmallTest
class PostTest {

    @Test
    fun newPost_ShouldHaveAnAuthor() {
        val post =
            Post("1234", User("Tester.Man", "tester@tester.com"), "Hello!", "Hello World!", 0)
        assertTrue(post.author.username.isNotEmpty())
        assertTrue("Tester.Man" == post.author.username)
    }

    @Test
    fun post_ShouldHaveEmptyConstructor() {
        val post = Post("", User("", ""), "", "", 0)
        assertTrue(post.toString().isNotEmpty())
    }


}