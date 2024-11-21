package com.hersonviveros.eliteapartments.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.EditText
import com.hersonviveros.eliteapartments.R
import java.io.File

fun EditText.convertStr(context: Context): String {
    val tex = this.text.toString()
    if (tex.isEmpty()) {
        this.error = context.getString(R.string.error_edt)
    }
    return tex
}

fun EditText.convertInt(context: Context): Int {
    var tex = this.text.toString()
    if (tex.isEmpty()) {
        this.error = context.getString(R.string.error_edt)
        tex = "0"
    }
    return tex.toInt()
}

@SuppressLint("ObsoleteSdkInt")
@Suppress("deprecation")
fun String.fromHtml(): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
        // we are using this flag to give a consistent behaviour
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun List<String>.convertStringListToUriList(): List<Uri> {
    return this.mapNotNull {
        val file = File(it)
        if (file.exists()) {
            Uri.fromFile(file)
        } else {
            null
        }
    }
}