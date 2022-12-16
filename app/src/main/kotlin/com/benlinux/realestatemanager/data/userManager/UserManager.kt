package com.benlinux.realestatemanager.data.userManager

import android.content.Context
import android.net.Uri
import com.benlinux.realestatemanager.data.userRepository.UserRepository
import com.benlinux.realestatemanager.ui.models.Realtor
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

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

        fun createUser(): Task<QuerySnapshot>? {
            return UserRepository.createUser()
        }

        fun getAllUsersData(): Task<List<Realtor?>?> {
            return Objects.requireNonNull(UserRepository.getAllUsersData()).continueWith { task ->
                task.result.toObjects(
                    Realtor::class.java
                )
            }
        }

        fun getUserData(): Task<Realtor?> {
            // Get the user from Firestore and cast it to a User model Object
            return Objects.requireNonNull(UserRepository.getUserData())!!.continueWith { task ->
                task.result?.toObject(
                    Realtor::class.java
                )
            }
        }

        fun updateUsername(username: String?) {
            UserRepository.updateUsername(username)
        }

        fun updateUserAvatarUrl(avatarUrl: Uri?) {
            UserRepository.uploadImage(avatarUrl, "avatar")?.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    UserRepository.updateUserAvatar(uri.toString())
                }
            }
        }

        fun deleteUserFromFirestore(context: Context?): Task<Void?>? {
            // Delete the user account from the Auth
            return UserRepository.deleteUserFromFirestore(context)
        }

    }


}