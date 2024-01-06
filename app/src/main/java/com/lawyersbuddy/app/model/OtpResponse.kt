package com.lawyersbuddy.app.model

data class OtpResponse(
    var `data`: OtpData,
    var message: String,
    var status: Int
)

data class OtpData(
    var otp: Int
)