package com.imn.whocalling.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat

fun String.logd(tag: String = "WHO_CALLING_LOG") {
    Log.d(tag, this)
}

fun Map.Entry<String, Boolean>.isDenied(activity: ComponentActivity) = ActivityCompat
    .shouldShowRequestPermissionRationale(
        activity,
        key,
    )

fun Map.Entry<String, Boolean>.isPermanentlyDenied(
    activity: ComponentActivity,
    preferences: SharedPreferences
) = ActivityCompat
    .shouldShowRequestPermissionRationale(
        activity,
        key,
    ).not() && value.not() && preferences.getBoolean(key, false)

fun Map.Entry<String, Boolean>.isIgnored(
    activity: ComponentActivity,
    preferences: SharedPreferences,
) = ActivityCompat
    .shouldShowRequestPermissionRationale(
        activity,
        key,
    ).not() && value.not() && preferences.getBoolean(key, false).not()

fun Context.openAppPermissionSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", this@openAppPermissionSettings.packageName, null)
    }
    this.startActivity(intent)
}

infix fun Context.toast(message: String){
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}