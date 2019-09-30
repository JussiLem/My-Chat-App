package com.chattyapp.mychatapp

import androidx.test.core.app.ActivityScenario
import androidx.test.filters.SmallTest
import org.junit.Test

@SmallTest
class MainActivityTest {

    @Test
    fun appLaunchesSuccesfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }
}