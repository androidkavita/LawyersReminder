package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName



data class AddHearingResponse (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : AddHearingData?   = AddHearingData()

)
data class AddHearingData (

    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("lawyer_id"       ) var lawyerId       : Int?    = null,
    @SerializedName("client_id"       ) var clientId       : Int?    = null,
    @SerializedName("case_id"         ) var caseId         : Int?    = null,
    @SerializedName("case_stage"      ) var caseStage      : String? = null,
    @SerializedName("case_status"     ) var caseStatus     : String? = null,
    @SerializedName("hearing_details" ) var hearingDetails : String? = null,
    @SerializedName("hearing_doc"     ) var hearingDoc     : String? = null,
    @SerializedName("hearing_date"    ) var hearingDate    : String? = null,
    @SerializedName("current_date"    ) var currentDate    : String? = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("updated_at"      ) var updatedAt      : String? = null

)