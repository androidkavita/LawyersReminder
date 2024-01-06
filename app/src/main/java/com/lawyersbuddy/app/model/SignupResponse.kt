package com.lawyersbuddy.app.model

data class SignupResponse(
    var `data`: SignupData,
    var message: String,
    var status: Int
)

data class SignupData(
    var address: String,
    var bar_association: String,
    var bar_council_number: String,
    var city: String,
    var email: String,
    var firm_name: String,
    var id: Int,
    var image: Any,
    var is_permission: Any,
    var mobile: String,
    var name: String,
    var state: String,
    var termcondition: Int,
    var type: Int,
    var zip_code: Any
)