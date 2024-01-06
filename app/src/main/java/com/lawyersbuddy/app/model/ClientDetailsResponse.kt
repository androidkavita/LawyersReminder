package com.lawyersbuddy.app.model

data class ClientDetailsResponse(
    var `data`: ClientDetailsData,
    var message: String,
    var status: Int
)

data class ClientDetailsData(
    var address: String,
    var created_at: String,
    var email: String,
    var id: Int,
    var lawyer_id: Int,
    var mobile: String,
    var image: String,
    var name: String,
    var status: Int,
    var updated_at: String


)