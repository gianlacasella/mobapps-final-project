package com.ifgarces.courseproject.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.ifgarces.courseproject.R


const val LOG_TAG :String = "_DEBUGLOG_" // logging output is not heavy, all of it will be labeled with this string. Used in `Logf`

/**
 * Debug log + string format. All log under the same tag stored at `LOG_TAG` constant, so it's
 * simpler.
 */
fun Logf(format :String, vararg args :Any?) {
    Log.d(LOG_TAG, format.format(*args))
}

/**
 * Toast + string format. Simpler call for a toast.
 */
fun Context.toastf(format :String, vararg args :Any?) {
    Toast.makeText(this, format.format(*args), Toast.LENGTH_LONG).show()
}

/**
 * `AlertDialog` with yes/no buttons.
 * @param title Dialog title.
 * @param message Dialog body.
 * @param onYesClicked Callback executed when the user presses the possitive button.
 * @param onNoClicked Callback executed when the user presses the negative button.
 * @param icon Dialog icon, placed to the left of the title. Must be a drawable resource ID.
 */
fun Context.yesNoDialog(
    title        :String, // note: if `title` is "", somehow the icon is not shown. Should use " " or similar insted.
    message      :String,
    onYesClicked :() -> Unit,
    onNoClicked  :() -> Unit = {},
    icon         :Int? = null // resource reference
) {
    val diag :AlertDialog.Builder = AlertDialog.Builder(this, R.style.myDialogTheme)
    //val diag :AlertDialog.Builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("Yes") { dialog :DialogInterface, _ :Int ->
            onYesClicked.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("No") { dialog :DialogInterface, _ :Int ->
            onNoClicked.invoke()
            dialog.dismiss()
        }
    if (icon != null) {
        diag.setIcon(icon)
    }
    diag.create().show()
}

/**
 * Simple `AlertDialog` that shows text information.
 * @param title Dialog title.
 * @param message Dialog body.
 * @param onDismiss Callback executed when the dialog is dismissed by the user.
 * @param icon Dialog icon, placed to the left of the title. Must be a drawable resource ID.
 */
fun Context.infoDialog(
    title     :String, // note: if `title` is "", somehow the icon is not shown. Should use " " or similar insted.
    message   :String?,
    onDismiss :() -> Unit = {},
    icon      :Int? = null // resource reference
) {
    val diag :AlertDialog.Builder = AlertDialog.Builder(this, R.style.myDialogTheme)
        .setTitle(title)
        .setMessage(message.toString())
        .setCancelable(false)
        .setPositiveButton(android.R.string.ok) { dialog :DialogInterface, _ :Int ->
            onDismiss.invoke()
            dialog.dismiss()
        }
    if (icon != null) {
        diag.setIcon(icon)
    }
    diag.create().show()
}
