package pro.edvard.alcotec.framework.presentation.util

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: Activity) {

    fun isWriteExternalStorageGranted(requestKey: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return if (ContextCompat.checkSelfPermission(
                    activity,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    requestKey
                )
                false
            }
        } else {
            true
        }
    }

}