package com.hersonviveros.eliteapartments.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hersonviveros.eliteapartments.utils.Constants.Companion.REQUEST_CODE_READ_MEMORY
import com.hersonviveros.eliteapartments.utils.Constants.Companion.REQUEST_CODE_WRITE_MEMORY

class Permissions(private val context: Activity) {

    fun takePermission(type: Int, callback: (Boolean) -> Unit) {
        if (type == REQUEST_CODE_WRITE_MEMORY) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                callback(true)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    callback(true)
                }
                requestPermission(REQUEST_CODE_WRITE_MEMORY)
            }
        } else if (type == REQUEST_CODE_READ_MEMORY) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    callback(true)
                } else {
                    requestPermission(REQUEST_CODE_READ_MEMORY)
                }
            } else {
                callback(true)
            }
        } else {
            callback(false)
        }
    }

    private fun requestPermission(type: Int) {
        when (type) {
            REQUEST_CODE_WRITE_MEMORY -> ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_CODE_WRITE_MEMORY
            )

            REQUEST_CODE_READ_MEMORY -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_MEMORY
                )
            }

            else -> {}
        }
    }
}