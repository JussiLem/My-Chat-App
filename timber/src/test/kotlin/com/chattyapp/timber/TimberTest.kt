package com.chattyapp.timber

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import timber.log.Timber

private data class Msg(val priority: Int, val tag: String?, val message: String?, val t: Throwable?)

private fun plantTestTree(ignoreMessage: Boolean): List<Msg> {
    val messages = mutableListOf<Msg>()
    Timber.plant(object : timber.log.Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            messages.add(
                Msg(
                    priority,
                    tag,
                    if (ignoreMessage) null else message,
                    t
                )
            )
        }
    })
    return messages
}

// provide no
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TimberTest {


}