package com.lawyersbuddy.app.model

data class AddClientResponse(
    var `data`: AddClientData,
    var message: String,
    var status: Int
)

data class AddClientData(
    var address: String,
    var created_at: String,
    var email: String,
    var id: Int,
    var lawyer_id: Int,
    var mobile: String,
    var name: String,
    var status: Int,
    var updated_at: String
)