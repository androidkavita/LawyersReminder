package com.lawyersbuddy.app.model

data class ContactUsResponse(
    var `data`: ContactUsData,
    var message: String,
    var status: Int
)

data class ContactUsData(
    var created_at: String,
    var email: String,
    var id: Int,
    var phone_number: String,
    var updated_at: String
)