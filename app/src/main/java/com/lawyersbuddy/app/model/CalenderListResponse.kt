package com.lawyersbuddy.app.model

data class CalenderListResponse(
    var `data`: List<CalenderListData>,
    var message: String,
    var status: Int
)

data class CalenderListData(
    var case_cnr_number: String,
    var case_id: Int,
    var case_stage: Any,
    var case_status: Any,
    var case_title: String,
    var client_id: Int,
    var client_name: String,
    var created_at: String,
    var current_date: String,
    var hearing_date: String,
    var hearing_details: Any,
    var hearing_doc: Any,
    var id: Int,
    var s_no: Int,
    var lawyer_id: Int,
    var updated_at: String
)