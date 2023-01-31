package com.benlinux.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri



class PropertyContentProvider : ContentProvider() {

    // The entry point of content provider
    // used to init futures variables
    override fun onCreate(): Boolean {
        return true
    }

    // Get room database data with cursor, from specified URI
    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    // Return MIME type associated to the URI, to identify with precision returned data
    override fun getType(uri: Uri): String? {
        return null
    }


    // Used to insert content values data from URI
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    // Used to delete content values data from URI
    override fun delete(
        uri: Uri,
        s: String?,
        strings: Array<String?>?
    ): Int {
        return 0
    }

    // Used to update content values data from URI
    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String?>?
    ): Int {
        return 0
    }
}