package com.benlinux.realestatemanager.data.propertyManager

import android.net.Uri
import com.benlinux.realestatemanager.data.propertyRepository.PropertyRepository
import com.google.firebase.storage.UploadTask

class PropertyManager {

    companion object {

        fun uploadImageToFirestore(imageUri: Uri?): UploadTask {
            // Delete the user account from the Auth
            return PropertyRepository.uploadImageToFirestore(imageUri)
        }

    }
}