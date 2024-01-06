package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName

data class HearingDetailsResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : HearingDetailData?

)

data class HearingDetailData (

    @SerializedName("id"              ) var id             : Int?    = null,
    @SerializedName("lawyer_id"       ) var lawyerId       : Int?    = null,
    @SerializedName("client_id"       ) var clientId       : Int?    = null,
    @SerializedName("case_id"         ) var caseId         : Int?    = null,
    @SerializedName("case_stage"      ) var caseStage      : String? = null,
    @SerializedName("case_status"     ) var caseStatus     : String? = null,
    @SerializedName("hearing_details" ) var hearingDetails : String? = null,
    @SerializedName("hearing_date"    ) var hearingDate    : String? = null,
    @SerializedName("current_date"    ) var currentDate    : String? = null,
    @SerializedName("created_at"      ) var createdAt      : String? = null,
    @SerializedName("updated_at"      ) var updatedAt      : String? = null,
    @SerializedName("case_title"      ) var caseTitle      : String? = null,
    @SerializedName("client_name"     ) var clientName     : String? = null,
    @SerializedName("final_date"     ) var final_date     : Int? = null,
    @SerializedName("is_auto_reminder"     ) var is_auto_reminder     : Int? = null,
    @SerializedName("file_path" ) var filePath : ArrayList<String>? = null,
    @SerializedName("edit_status" )     var editStatus:String,
    @SerializedName("file_detail"    ) var file_detail    : ArrayList<file_detail> = arrayListOf()
)
data class file_detail (

    @SerializedName("file_name"              ) var file_name             : String?    = null,
    @SerializedName("file_type"              ) var file_type             : Int?    = null,
    @SerializedName("path_name"       ) var path_name       : String?    = null
)