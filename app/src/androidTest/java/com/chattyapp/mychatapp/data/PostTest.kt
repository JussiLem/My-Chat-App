package com.chattyapp.mychatapp.data

import androidx.test.filters.SmallTest
import org.junit.Test

import org.junit.Assert.*

@SmallTest
class PostTest {

    @Test
    fun newPost_ShouldHaveAnAuthor() {
        val post = Post("1234", "Tester", "Hello!", "Hello World!", 0)
        assertTrue(post.author!!.isNotEmpty())
    }


}