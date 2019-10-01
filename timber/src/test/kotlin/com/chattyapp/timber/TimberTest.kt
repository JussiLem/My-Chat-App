package com.chattyapp.timber

import android.util.Log
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

private data class Msg(val priority: Int, val tag: String?, val message: String?, val t: Throwable?)

// provide no
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TimberTest {

    @Before
    @After
    fun setup() {
        Timber.uprootAll()
    }

    @Test
    fun logMessages() {
        val messages = plantTestTree(ignoreMessage = false)

        v { "Verbose" }
        Timber.v { "Verbose" }
        Timber.tag("Custom").v { "Verbose" }
        d { "Debug" }
        Timber.d { "Debug" }
        Timber.tag("Custom").d { "Debug" }
        i { "Info" }
        Timber.i { "Info" }
        Timber.tag("Custom").i { "Info" }
        w { "Warn" }
        Timber.w { "Warn" }
        Timber.tag("Custom").w { "Warn" }
        e { "Error" }
        Timber.e { "Error" }
        Timber.tag("Custom").e { "Error" }
        wtf { "Assert" }
        Timber.wtf { "Assert" }
        Timber.tag("Custom").wtf { "Assert" }

        assertThat(messages).containsExactly(
            Msg(Log.VERBOSE, "TimberTest", "Verbose", null),
            Msg(Log.VERBOSE, "TimberTest", "Verbose", null),
            Msg(Log.VERBOSE, "Custom", "Verbose", null),
            Msg(Log.DEBUG, "TimberTest", "Debug", null),
            Msg(Log.DEBUG, "TimberTest", "Debug", null),
            Msg(Log.DEBUG, "Custom", "Debug", null),
            Msg(Log.INFO, "TimberTest", "Info", null),
            Msg(Log.INFO, "TimberTest", "Info", null),
            Msg(Log.INFO, "Custom", "Info", null),
            Msg(Log.WARN, "TimberTest", "Warn", null),
            Msg(Log.WARN, "TimberTest", "Warn", null),
            Msg(Log.WARN, "Custom", "Warn", null),
            Msg(Log.ERROR, "TimberTest", "Error", null),
            Msg(Log.ERROR, "TimberTest", "Error", null),
            Msg(Log.ERROR, "Custom", "Error", null),
            Msg(Log.ASSERT, "TimberTest", "Assert", null),
            Msg(Log.ASSERT, "TimberTest", "Assert", null),
            Msg(Log.ASSERT, "Custom", "Assert", null)
        )
    }

    private fun plantTestTree(ignoreMessage: Boolean): List<Msg> {
        val messages = mutableListOf<Msg>()
        Timber.plant(object : timber.log.Timber.DebugTree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                messages.add(Msg(priority, tag, if (ignoreMessage) null else message, t))
            }
        })
        return messages
    }

}