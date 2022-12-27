package com.benlinux.realestatemanager.data.propertyRepository

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class PropertyRepository {

    companion object {

        // FIRESTORE DATA
        private const val COLLECTION_NAME = "properties"
        private const val PICTURES_FOLDER = "pictures"

        // Get the Collection Reference in Firestore Database
        fun getPropertiesCollection(): CollectionReference {
            return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
        }

        // Upload image from device to firebase storage
        fun uploadImageToFirestore(imageUri: Uri?): UploadTask {
            val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
            val storageRef = FirebaseStorage.getInstance().reference
            val picturesRef = storageRef.child("$PICTURES_FOLDER/$uuid")
            return picturesRef.putFile(imageUri!!)
        }

    }
}