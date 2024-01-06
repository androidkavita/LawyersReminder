package com.lawyersbuddy.app.model

data class HomeSearchResponse(
    var `data`: List<HomeSearchData>,
    var message: String,
    var status: Int
)

data class HomeSearchData(
    var address: String,
    var created_at: String,
    var email: String,
    var id: Int,
    var image: Any,
    var lawyer_id: Int,
    var mobile: String,
    var name: String,
    var status: Int,
    var case_id: Int,
    var is_auto_reminder: Int,
    var final_date: Int,
    var updated_at: String,
    var client_id: String,
    var start_date: String,
    var first_hearing_date: String,
    var opponent_party_name: String,
    var opponent_party_lawyer_name: String,
    var case_stage: String,
    var case_status: String,
    var judge_name: String,
    var cnr_number: String,
    var court_name: String,
    var case_title: String,
    var client_name: String,
    var hearing_details: String,
    var hearing_doc: String,
    var hearing_date: String,
    var current_date: String,

)