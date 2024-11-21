package com.hersonviveros.eliteapartments.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions(private val context: Activity) {

    fun checkAndRequestPermission(type: PermissionType, callback: (Boolean) -> Unit) {
        when (type) {
            PermissionType.WRITE_STORAGE -> handleWriteStoragePermission(callback)
            PermissionType.READ_STORAGE -> handleReadStoragePermission(callback)
            else -> callback(false)
        }
    }

    private fun handleWriteStoragePermission(callback: (Boolean) -> Unit) {
        when {
            // For Android 13 (Tiramisu) and above, no storage permissions needed
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> callback(true)

            // Check if permission already granted
            isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> callback(true)

            // Request permission if not granted
            else -> requestStoragePermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                REQUEST_CODE_WRITE_STORAGE,
                callback
            )
        }
    }

    private fun handleReadStoragePermission(callback: (Boolean) -> Unit) {
        when {
            // For Android 13 (Tiramisu) and above, no storage permissions needed
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> callback(true)

            // Check if permission already granted
            isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) -> callback(true)

            // Request permission if not granted
            else -> requestStoragePermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                REQUEST_CODE_READ_STORAGE,
                callback
            )
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission(
        permission: String,
        requestCode: Int,
        callback: (Boolean) -> Unit
    ) {
        // Store callback for later use in onRequestPermissionsResult
        permissionCallbacks[requestCode] = callback

        ActivityCompat.requestPermissions(
            context,
            arrayOf(permission),
            requestCode
        )
    }

    // Must be called from the Activity's onRequestPermissionsResult
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        val callback = permissionCallbacks[requestCode]

        val isGranted = grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED

        callback?.invoke(isGranted)
        permissionCallbacks.remove(requestCode)
    }

    enum class PermissionType {
        WRITE_STORAGE,
        READ_STORAGE,
        OTHER
    }

    companion object {
        private const val REQUEST_CODE_WRITE_STORAGE = 1001
        private const val REQUEST_CODE_READ_STORAGE = 1002

        // Stores callbacks for permission requests
        private val permissionCallbacks = mutableMapOf<Int, ((Boolean) -> Unit)?>()
    }
}