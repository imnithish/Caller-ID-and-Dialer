package com.imn.whocalling.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.imn.whocalling.data.MockyResponse
import com.imn.whocalling.network.ApiService
import retrofit2.Response
import java.util.Date
import java.util.Locale

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

infix fun Context.toast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

suspend fun ApiService.fetchMockyAPI(): ResultWrapper<Response<MockyResponse>> {
    return safeApiCall {
        getMockyData()
    }
}

fun Date.formatDate(): String? {
    try {
        val formatter =
            SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val formattedDate = formatter.format(this)
        return formattedDate
    } catch (e: Exception) {
        "$e".logd()
        return null
    }
}

fun Int.formatDuration(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val secs = this % 60

    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0) append("${minutes}m ")
        if (secs > 0 || (hours == 0 && minutes == 0)) append("${secs}s")
    }.trim()
}

infix fun Context.dialNumber(phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL).apply {
        data = "tel:$phoneNumber".toUri()
    }
    this.startActivity(intent)
}