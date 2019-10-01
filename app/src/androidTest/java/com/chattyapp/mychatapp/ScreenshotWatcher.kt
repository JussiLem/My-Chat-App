package com.chattyapp.mychatapp

import android.graphics.Bitmap
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

class ScreenshotWatcher : TestWatcher() {

    override fun succeeded(description: Description) {
        val locale = InstrumentationRegistry.getInstrumentation().targetContext
            .resources
            .configuration
            .locales
            .get(0)

        captureSceenshot(description.methodName + "_" + locale.toLanguageTag())
    }

    override fun failed(e: Throwable?, description: Description) {
        captureSceenshot(description.methodName + "_fail")
    }

    private fun captureSceenshot(name: String) {
        val capture = Screenshot.capture()
        capture.format = Bitmap.CompressFormat.PNG
        capture.name = name

        try {
            capture.process()
        } catch (ex: IOException) {
            throw IllegalStateException(ex)
        }
    }



}