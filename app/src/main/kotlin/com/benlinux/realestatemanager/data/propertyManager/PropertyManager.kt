package com.benlinux.realestatemanager.data.propertyManager

import android.net.Uri
import com.benlinux.realestatemanager.data.propertyRepository.PropertyRepository
import com.benlinux.realestatemanager.ui.models.Property
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask
import java.util.*

class PropertyManager {

    companion object {

        fun getAllPropertiesData(): Task<List<Property?>?> {
            return Objects.requireNonNull(PropertyRepository.getAllPropertiesData()).continueWith { task ->
                task.result.toObjects(
                    Property::class.java
                )
            }
        }

        fun createProperty(property: Property): Task<QuerySnapshot> {
            return PropertyRepository.createProperty(property)
        }

        fun uploadImageToFirestore(imageUri: Uri?): UploadTask {
            // Delete the user account from the Auth
            return PropertyRepository.uploadImageToFirestore(imageUri)
        }

        fun updateProperty(property: Property): Task<Void> {
            return PropertyRepository.updateProperty(property)
        }

    }
}