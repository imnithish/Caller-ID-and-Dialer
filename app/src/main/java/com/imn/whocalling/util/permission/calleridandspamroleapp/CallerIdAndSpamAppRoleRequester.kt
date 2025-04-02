package com.imn.whocalling.util.permission.calleridandspamroleapp

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.imn.whocalling.util.logd
import com.imn.whocalling.util.permission.PermissionState

@RequiresApi(Build.VERSION_CODES.Q)
class CallerIdAndSpamAppRoleRequester(
    activity: ComponentActivity
) {
    private val requestedRole: String = RoleManager.ROLE_CALL_SCREENING
    private val roleManager = activity.getSystemService(Context.ROLE_SERVICE) as RoleManager
    private var onRoleRequestResult: ((PermissionState) -> Unit)? = null
    private val requestRoleLauncher: ActivityResultLauncher<Intent>

    init {
        try {
            requestRoleLauncher = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) {
                onRoleRequestResult?.invoke(
                    when {
                        it.resultCode == Activity.RESULT_OK -> {
                            PermissionState.GRANTED
                        }

                        else -> {
                            PermissionState.DENIED
                        }
                    }
                )
            }
        } catch (exception: Exception) {
            "$exception".logd()
            throw Exception()
        }
    }

    fun isRoleGranted(): Boolean = roleManager.isRoleHeld(requestedRole)

    fun requestRole(
        onRoleRequestResult: ((PermissionState) -> Unit)? = null,
    ) {
        this.onRoleRequestResult = onRoleRequestResult
        val intent = roleManager.createRequestRoleIntent(requestedRole)
        requestRoleLauncher.launch(intent)
    }

}