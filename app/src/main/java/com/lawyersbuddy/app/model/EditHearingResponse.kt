package com.lawyersbuddy.app.model

data class EditHearingResponse(
    var `data`: EditHearingData,
    var message: String,
    var status: Int
)

data class EditHearingData(
    var case_id: Int,
    var case_stage: String,
    var case_status: String,
    var client_id: Int,
    var created_at: String,
    var current_date: String,
    var hearing_date: String,
    var hearing_details: String,
    var hearing_doc: String,
    var id: Int,
    var lawyer_id: Int,
    var updated_at: String,
    var edit_status:String,
)