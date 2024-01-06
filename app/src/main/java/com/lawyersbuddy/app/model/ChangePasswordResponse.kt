package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : String? = null

)