package com.lawyersbuddy.app.model

data class LoginResponse(
    var `data`: LoginData,
    var message: String,
    var status: Int
)

data class LoginData(
    var address: String,
    var bar_association: String,
    var bar_council_number: String,
    var city: String,
    var created_at: String,
    var email: String,
    var email_verified_at: Any,
    var firm_name: Any,
    var id: Int,
    var image: String,
    var is_permission: Int,

    var lawyer_id: Int,
    var mobile: String,
    var name: String,
    var state: String,
    var status: Int,
    var termcondition: Int,
    var token: String,
    var type: Int,
    var updated_at: String,
    var is_user: Int,
    var zip_code: Any,
    var city_id: Int,
    var state_id: Int,
    var notification_status: Int
)