package com.benlinux.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.benlinux.realestatemanager.database.REMDatabase
import com.benlinux.realestatemanager.utils.Constants.Companion.AUTHORITY
import com.benlinux.realestatemanager.utils.Constants.Companion.CODE_PROPERTY_DIR
import com.benlinux.realestatemanager.utils.Constants.Companion.CODE_PROPERTY_ITEM
import com.benlinux.realestatemanager.utils.Constants.Companion.COLLECTION_PROPERTIES


class PropertyContentProvider : ContentProvider() {

    companion object{
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$COLLECTION_PROPERTIES")
        val URI_COLLECTION: Uri = Uri.parse("content://$AUTHORITY/$COLLECTION_PROPERTIES")
    }

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, COLLECTION_PROPERTIES, CODE_PROPERTY_DIR)
        addURI(AUTHORITY, "$COLLECTION_PROPERTIES/*", CODE_PROPERTY_ITEM)
    }

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
        var id: Int? = null
        try {
            id = uri.lastPathSegment?.toInt()
        } catch (e: Exception) {
            Log.d("Parse Int", "no ID to convert")
        }
        val database = REMDatabase.getInstance(context!!)
        return when(uriMatcher.match(uri)){
            CODE_PROPERTY_ITEM -> id?.let { database.propertyDao().getPropertyWithCursor(id) }
            CODE_PROPERTY_DIR -> database.propertyDao().getAllPropertiesWithCursor()
            else -> throw IllegalArgumentException("Query doesn't exist $uri")
        }
    }

    // Return MIME type associated to the URI, to identify with precision returned data
    override fun getType(uri: Uri): String {
        return when(uriMatcher.match(uri)){
            CODE_PROPERTY_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$COLLECTION_PROPERTIES"
            CODE_PROPERTY_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$COLLECTION_PROPERTIES"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }


    // Used to insert content values data from URI
    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        throw IllegalArgumentException("Permission denied to insert $uri")
    }

    // Used to delete content values data from URI
    override fun delete(
        uri: Uri,
        s: String?,
        strings: Array<String?>?
    ): Int {
        throw IllegalArgumentException("Permission denied to delete $uri")
    }

    // Used to update content values data from URI
    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String?>?
    ): Int {
        throw IllegalArgumentException("Permission denied to update $uri")
    }
}