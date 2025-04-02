package com.imn.whocalling.util.permission.systemalertwindow

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity

class SystemAlertWindowPermission(
    val activity: ComponentActivity,
) {

     fun isGranted() =
         Settings.canDrawOverlays(activity)

     fun requestPermission() {
         val intent = Intent(
             Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
             Uri.fromParts("package", activity.packageName, null),
         )
         intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
         activity.startActivity(intent)
     }
}