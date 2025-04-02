package com.imn.whocalling.util.permission.systemalertwindow

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.imn.whocalling.util.logd
import com.imn.whocalling.util.permission.PermissionState

class SystemAlertWindowPermissionRequester(
    private val activity: ComponentActivity,
) {

    private val systemAlertWindowPermission = SystemAlertWindowPermission(activity)
    private var onSystemAlertWindowPermissionStateResult: ((PermissionState) -> Unit)? =
        null
    private var isFirstOnStartCallback = true

    public fun isPermissionGranted(): Boolean = systemAlertWindowPermission.isGranted()

    private val activityLifecycleObserver: LifecycleEventObserver by lazy {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isFirstOnStartCallback = if (isFirstOnStartCallback) {
                    false
                } else {
                    activity.lifecycle.removeObserver(activityLifecycleObserver)
                    onSystemAlertWindowPermissionStateResult?.invoke(
                        if (systemAlertWindowPermission.isGranted()) PermissionState.GRANTED else PermissionState.DENIED
                    )
                    true
                }
            }
        }
    }


    public fun requestPermission(
        onSpecialPermissionRequestResult: ((PermissionState) -> Unit)? = null,
    ) {
        this.onSystemAlertWindowPermissionStateResult = onSpecialPermissionRequestResult
        try {
            systemAlertWindowPermission.requestPermission()
            activity.lifecycle.addObserver(activityLifecycleObserver)
        } catch (exception: Exception) {
            "$exception".logd()
        }
    }


}