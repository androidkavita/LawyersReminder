package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName

data class AddCaseResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : AddCaseData?   = AddCaseData()

)

data class AddCaseData(

@SerializedName("id"                         ) var id                      : Int?    = null,
@SerializedName("lawyer_id"                  ) var lawyerId                : Int?    = null,
@SerializedName("client_id"                  ) var clientId                : Int?    = null,
@SerializedName("start_date"                 ) var startDate               : String? = null,
@SerializedName("first_hearing_date"         ) var firstHearingDate        : String? = null,
@SerializedName("opponent_party_name"        ) var opponentPartyName       : String? = null,
@SerializedName("opponent_party_lawyer_name" ) var opponentPartyLawyerName : String? = null,
@SerializedName("case_stage"                 ) var caseStage               : String? = null,
@SerializedName("case_status"                ) var caseStatus              : String? = null,
@SerializedName("judge_name"                 ) var judgeName               : String? = null,
@SerializedName("cnr_number"                 ) var cnrNumber               : String? = null,
@SerializedName("court_name"                 ) var courtName               : String? = null,
@SerializedName("case_title"                 ) var caseTitle               : String? = null,
@SerializedName("case_detail"                ) var caseDetail              : String? = null,
@SerializedName("created_at"                 ) var createdAt               : String? = null,
@SerializedName("updated_at"                 ) var updatedAt               : String? = null

)