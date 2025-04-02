package com.imn.whocalling.util.permission.telephonyandcontacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.imn.whocalling.util.isDenied
import com.imn.whocalling.util.isIgnored
import com.imn.whocalling.util.isPermanentlyDenied
import com.imn.whocalling.util.logd
import com.imn.whocalling.util.permission.PermissionState

class TelephonyAndContactsPermissionRequester(
    private val activity: ComponentActivity,
) {

    val requestedPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG
    )

    private var onTelephonyAndContactsPermissionResult: ((Map<String, PermissionState>) -> Unit)? =
        null
    private val requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    init {
        try {
            val preferences =
                activity.getSharedPreferences("PERMISSIONS_PREFERENCES", Context.MODE_PRIVATE)

            requestPermissionLauncher = activity.registerForActivityResult(
                RequestMultiplePermissions(),
            ) { permissionsGrants ->
                val permissionsRequestResult = permissionsGrants.entries.associate { entry ->
                    entry.key to when {
                        entry.isDenied(activity) -> {
                            preferences.edit { putBoolean(entry.key, true) }
                            PermissionState.DENIED
                        }

                        entry.isPermanentlyDenied(activity, preferences) -> {
                            PermissionState.PERMANENTLY_DENIED
                        }

                        entry.isIgnored(activity, preferences) -> {
                            PermissionState.IGNORED
                        }

                        else -> {
                            PermissionState.GRANTED
                        }
                    }
                }
                onTelephonyAndContactsPermissionResult?.invoke(permissionsRequestResult)
            }
        } catch (exception: Exception) {
            "$exception".logd()
            throw Exception()
        }
    }

    fun isPermissionGranted(
        permission: String,
    ): Boolean {
        val isGranted = ActivityCompat.checkSelfPermission(
            activity,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
        return isGranted
    }

    fun areAllPermissionsGranted(): Boolean = requestedPermissions.none { permission ->
        isPermissionGranted(permission).not()
    }


    fun requestPermissions(
        onRuntimePermissionsRequestResult: ((Map<String, PermissionState>) -> Unit)? = null,
    ) {
        this.onTelephonyAndContactsPermissionResult = onRuntimePermissionsRequestResult
        requestPermissionLauncher.launch(requestedPermissions)
    }

}