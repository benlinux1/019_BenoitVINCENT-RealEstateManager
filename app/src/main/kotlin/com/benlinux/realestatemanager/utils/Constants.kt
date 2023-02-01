package com.benlinux.realestatemanager.utils

import android.Manifest

class Constants {
    companion object {
        // Permissions for picture picking and camera
        const val PHOTO_ACCESS_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val CAMERA_ACCESS_PERMISSION = Manifest.permission.CAMERA
        const val RC_IMAGE_PERMS = 100
        const val IMAGE_CAPTURE_CODE = 1001

        // CONTENT PROVIDER
        const val AUTHORITY = "com.benlinux.realestatemanager.provider"
        const val CODE_PROPERTY_DIR = 1
        const val CODE_PROPERTY_ITEM = 2

        // FIRESTORE DATA
        const val COLLECTION_PROPERTIES = "properties"
        const val PICTURES_FOLDER = "pictures"

        const val COLLECTION_USERS = "users"
        const val FIRSTNAME_FIELD = "firstName"
        const val LASTNAME_FIELD = "lastName"
        const val EMAIL_FIELD = "email"
        const val AVATAR_FIELD = "avatarUrl"
        const val REALTOR_FIELD = "realtor"
        const val FAVORITES_FIELD = "favorites"
        const val PROPERTIES_FIELD = "realtorProperties"
    }
}