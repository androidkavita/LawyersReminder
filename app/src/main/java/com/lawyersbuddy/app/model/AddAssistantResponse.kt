package com.lawyersbuddy.app.model

data class AddAssistantResponse(
    var `data`: AddAssistantData,
    var message: String,
    var status: Int
)

data class AddAssistantData(
    var address: Any,
    var bar_association: Any,
    var bar_council_number: Any,
    var city: Any,
    var created_at: String,
    var email: String,
    var email_verified_at: Any,
    var firm_name: Any,
    var id: Int,
    var image: Any,
    var is_permission: Int,
    var lawyer_id: Int,
    var mobile: String,
    var name: String,
    var state: Any,
    var status: Int,
    var termcondition: Any,
    var type: Int,
    var updated_at: String,
    var zip_code: Any
)