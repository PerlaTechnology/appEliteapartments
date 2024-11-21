package com.hersonviveros.eliteapartments.ui.view

import android.annotation.SuppressLint
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.rengwuxian.materialedittext.MaterialEditText

fun MaterialEditText.converters(): String {
    return this.text.toString().trim()
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