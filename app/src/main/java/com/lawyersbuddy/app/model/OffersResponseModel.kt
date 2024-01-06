package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName


data class OffersResponseModel (

    @SerializedName("status"  ) var status  : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<OffersData> = arrayListOf()

)
data class OffersData (

    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("code"       ) var code      : String? = null,
    @SerializedName("discount"   ) var discount  : Int?    = null,
    @SerializedName("validated"  ) var validated : String? = null,
    @SerializedName("T_C"        ) var TC        : String? = null,
    @SerializedName("status"     ) var status    : Int?    = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null

)