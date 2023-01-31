package com.benlinux.realestatemanager.ui.models

data class PropertyAddress(var streetNumber: String? = null,
                      var streetName: String,
                      var complement: String? = null,
                      var postalCode: String,
                      var city: String,
                      var country: String

)
{
    // Constructor used to avoid no-arg constructor & deserialization problems with firebase database
    constructor() : this(
        null,
        "",
        null,
        "",
        "",
        "",
    )
}