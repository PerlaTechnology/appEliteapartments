package com.hersonviveros.eliteapartments.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.EditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hersonviveros.eliteapartments.R
import com.hersonviveros.eliteapartments.ui.adapter.ImagesAdapter
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

//Extension function para configurar drag and drop
fun RecyclerView.setupDragDrop(adapter: ImagesAdapter) {
    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            (recyclerView.adapter as? ImagesAdapter)?.onItemMove(fromPosition, toPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            //No implementamos swipe
        }
    })

    itemTouchHelper.attachToRecyclerView(this)
}