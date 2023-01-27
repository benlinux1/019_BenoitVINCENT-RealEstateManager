package com.benlinux.realestatemanager.data.userManager

import android.content.Context
import android.net.Uri
import com.benlinux.realestatemanager.data.userRepository.UserRepository
import com.benlinux.realestatemanager.ui.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot

class UserManager {

    companion object {

        fun getCurrentUser(): FirebaseUser? {
            return UserRepository.getCurrentUser()
        }

        fun isCurrentUserLogged(): Boolean {
            return getCurrentUser() != null
        }

        fun signOut(context: Context?): Task<Void?> {
            return UserRepository.signOut(context)
        }

        fun deleteUser(context: Context?): Task<Void?> {
            return UserRepository.deleteUser(context)
        }

        fun createUser(user: User): Task<QuerySnapshot>? {
            return UserRepository.createUser(user)
        }


        fun getUserData(): Task<User?>? {
            // Get the user from Firestore and cast it to a User model Object
            return UserRepository.getUserData()?.continueWith { task ->
                task.result?.toObject(
                    User::class.java
                )
            }
        }

        fun getUserDataById(userId: String): Task<User?> {
            // Get the user from Firestore and cast it to a User model Object
            return UserRepository.getUserDataById(userId).continueWith { task ->
                task.result?.toObject(
                    User::class.java
                )
            }
        }

        fun updateUserEmailInFirestore(email: String?) {
            UserRepository.updateUserEmailInFirestore(email)
        }

        fun updateUserFirstName(firstname: String?) {
            UserRepository.updateUserFirstname(firstname)
        }

        fun updateUserLastName(lastname: String?) {
            UserRepository.updateUserLastname(lastname)
        }

        fun updateUserAvatarUrl(avatarUrl: Uri?) {
            UserRepository.uploadImage(avatarUrl, "avatar").addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    UserRepository.updateUserAvatar(uri.toString())
                }
            }
        }

        fun updateIsRealtor(isRealtor: Boolean?) {
            UserRepository.updateIsRealtor(isRealtor)
        }

        fun addPropertyToFavorites(propertyId: String?) {
            UserRepository.addPropertyToFavorites(propertyId)
        }

        fun removePropertyFromFavorites(propertyId: String?) {
            UserRepository.removePropertyFromFavorites(propertyId)
        }

        fun addPropertyToRealtorProperties(propertyId: String?) {
            UserRepository.addPropertyToRealtorProperties(propertyId)
        }

        fun deleteUserFromFirestore(): Task<Void?> {
            // Delete the user account from the Auth
            return UserRepository.deleteUserFromFirestore()
        }
    }
}