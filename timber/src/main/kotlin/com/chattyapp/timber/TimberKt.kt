
package com.chattyapp.timber

import timber.log.Timber

/**
 * Static methods on the Timber object
 */
object Timber {
    // Log a verbose exception and a message that will be evaluated lazily when the message is printed
    @JvmStatic inline fun v(t: Throwable? = null, message: () -> String) = log { Timber.v(t, message()) }
    @JvmStatic inline fun v(t: Throwable?) = Timber.v(t)

    /** Log a debug exception and a message that will be evaluated lazily when the message is printed */
    @JvmStatic inline fun d(t: Throwable? = null, message: () -> String) = log { Timber.d(t, message()) }
    @JvmStatic inline fun d(t: Throwable?) = Timber.d(t)

    /** Log a warning exception and a message that will be evaluated lazily when the message is printed */
    @JvmStatic inline fun w(t: Throwable? = null, message: () -> String) = log { Timber.w(t, message()) }
    @JvmStatic inline fun w(t: Throwable?) = Timber.w(t)

    /// Log an assert exception and a message that will be evaluated lazily when the message is printed
    @JvmStatic inline fun wtf(t: Throwable? = null, message: () -> String) = log { Timber.wtf(t, message()) }
    @JvmStatic inline fun wtf(t: Throwable?) = Timber.wtf(t)

    /** Log an error exception and a message that will be evaluated lazily when the message is printed */
    @JvmStatic inline fun e(t: Throwable? = null, message: () -> String) = log { Timber.e(t, message()) }
    @JvmStatic inline fun e(t: Throwable?) = Timber.e(t)

    /** Add a new logging tree. */
    @JvmStatic inline fun plant(tree: Timber.Tree) = Timber.plant(tree)

    /** A [Timber.Tree] for debug builds. Automatically infers the tag from the calling class. */
    @JvmStatic inline fun DebugTree() = Timber.DebugTree()

    /** Set a one-time tag for use on the next logging call. */
    @JvmStatic inline fun tag(tag: String): Timber.Tree = Timber.tag(tag)

}



// extensions on tree
/** Log a verbose exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.v(t: Throwable? = null, message: () -> String) =
    log { v(t, message()) }

/** Log a debug exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.d(t: Throwable? = null, message: () -> String) = log { d(t, message()) }

/** Log an error exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.e(t: Throwable? = null, message: () -> String) =
    log { e(t, message()) }

/** Log an assert exception and a message that will be evaluated lazily when the message is printed */
inline fun Timber.Tree.wtf(t: Throwable? = null, message: () -> String) =
    log { wtf(t, message()) }


@PublishedApi
internal inline fun log(block: () -> Unit) {
    if (Timber.treeCount() > 0) block()
}