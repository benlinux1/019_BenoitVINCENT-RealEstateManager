package com.benlinux.realestatemanager.ui.models

data class Picture(var url: String,
                   var room: String)

{
    // Constructor used to avoid no-arg constructor & deserialization problems with firebase database
    constructor() : this(
        "",
        "",
    )
}