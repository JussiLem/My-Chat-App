package com.chattyapp.mychatapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import kotlin.random.Random


@LargeTest
class NewPostTest {

    @Rule @JvmField var mActivityTestRule = ActivityTestRule(SignInActivity::class.java)

    @Rule @JvmField var mScreenshotWatcher = ScreenshotWatcher()

    @Rule @JvmField var mGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)


    @Before
    fun setup() {
        mActivityTestRule.activity
    }

    @Test
    fun newPostTest() {
        // Generate user and post content
        val username = "user" + randomDigits()
        val email = "$username@example.com"
        val password = "testuser"
        val postTitle = "Title " + randomDigits()
        val postContent = "Content " + randomDigits()

        // go back to the sign in screen if we're logged in from a previous test
        logOutIfPossible()

        // Select email field
        val appCompatEditText = onView(allOf(withId(R.id.fieldEmail),
            withParent(withId(R.id.layoutEmailPassword))))
        appCompatEditText.perform(click())

        // Enter email address
        val appCompatEditText2 = onView(allOf(withId(R.id.fieldEmail),
            withParent(withId(R.id.layoutEmailPassword)),
            isDisplayed()))
        appCompatEditText2.perform(replaceText(email))

        // Enter password
        val appCompatEditText3 = onView(
            allOf(withId(R.id.fieldPassword),
                withParent(withId(R.id.layoutEmailPassword)),
                isDisplayed()))
        appCompatEditText3.perform(replaceText(password))

        // Click submit button
        val floatingActionButton = onView(
            allOf(withId(R.id.fabNewPost), isDisplayed())
        )
        floatingActionButton.perform(click())


    }

    private fun logOutIfPossible() {
        try {
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
            onView(withText(R.string.menu_login))
        } catch (e: NoMatchingViewException) {

        }
    }

    private fun randomDigits(): String {
        val random = List(10) { Random.nextInt(0, 99999999)}
        return random.toString()
    }


}