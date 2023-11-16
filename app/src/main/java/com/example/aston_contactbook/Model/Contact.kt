package com.example.aston_contactbook.Model

import java.io.Serializable

data class Contact(
    var id: Int?,
    var firstName: String,
    var lastName: String,
    var phoneNumber: String,
    var isChecked: Boolean
): Serializable