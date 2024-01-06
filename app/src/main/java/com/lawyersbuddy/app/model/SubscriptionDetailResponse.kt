package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName

data class SubscriptionDetailResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : SubscriptionDetailData

)


data class SubscriptionDetailData (

    @SerializedName("id"          ) var id          : Int?    = null,
    @SerializedName("title"       ) var title       : String? = null,
    @SerializedName("per_day_sms" ) var perDaySms   : Int?    = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("days"        ) var days        : Int?    = null,
    @SerializedName("amount"      ) var amount      : Int?    = null,
    @SerializedName("status"      ) var status      : Int?    = null,
    @SerializedName("created_at"  ) var createdAt   : String? = null,
    @SerializedName("updated_at"  ) var updatedAt   : String? = null

)