package com.lawyersbuddy.app.model

import com.lawyersbuddy.app.baseClasses.BaseModel

data class CasesResponse(
    var `data`: ArrayList<CasesData>,
    var message: String,
    var status: Int
):BaseModel()

data class CasesData(
    var case_detail: String,
    var case_stage: String,
    var case_status: String,
    var case_title: String,
    var client_id: Int,
    var cnr_number: String,
    var court_name: String,
    var created_at: String,
    var currentdate: Any,
    var first_hearing_date: String,
    var hearing_date: Any,
    var hearing_details: Any,
    var hearing_doc: Any,
    var id: Int,
    var judge_name: String,
    var lawyer_id: Int,
    var opponent_party_lawyer_name: Any,
    var opponent_party_name: String,
    var start_date: String,
    var updated_at: String,
    var client_name:String
):BaseModel()