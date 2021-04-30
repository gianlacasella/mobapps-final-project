package com.ifgarces.courseproject

import android.content.Context
import android.util.Log
import android.widget.Toast


const val LOG_TAG    :String = "_DEBUGLOG_" // logging output is not heavy, all of it will be labeled with this string. Used in `Logf`

/**
 * Debug log + string format. All log under the same tag stored at `LOG_TAG` constant, so it's
 * simpler.
 */
fun Logf(format :String, vararg args :Any?) {
    Log.d(LOG_TAG, format.format(*args))
}

/**
 * Quick toast + string format.
 */
fun Context.toastf(format :String, vararg args :Any?) {
    Toast.makeText(this, format.format(*args), Toast.LENGTH_LONG).show()
}
