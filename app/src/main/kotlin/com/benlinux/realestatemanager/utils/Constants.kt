package com.benlinux.realestatemanager.utils

import android.Manifest

class Constants {
    companion object {
        // Permissions for picture picking and camera
        const val PHOTO_ACCESS_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val CAMERA_ACCESS_PERMISSION = Manifest.permission.CAMERA
        const val RC_IMAGE_PERMS = 100
        const val IMAGE_CAPTURE_CODE = 1001
    }
}