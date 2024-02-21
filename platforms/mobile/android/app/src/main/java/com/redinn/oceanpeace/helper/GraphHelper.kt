package com.redinn.oceanpeace.helper

import android.content.Context
import java.lang.ref.WeakReference
@Suppress("MemberVisibilityCanBePrivate")
object GraphHelper {
    private var context: WeakReference<Context>? = null
    fun setup(c: Context) {
        context = WeakReference(c)
    }
}
