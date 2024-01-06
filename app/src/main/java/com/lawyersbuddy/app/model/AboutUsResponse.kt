package com.lawyersbuddy.app.model

data class AboutUsResponse(
    var `data`: AboutUsData,
    var message: String,
    var status: Int
)

data class AboutUsData(
    var created_at: String,
    var description: String,
    var id: Int,
    var updated_at: String
)