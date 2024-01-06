package com.lawyersbuddy.app.model

import com.lawyersbuddy.app.baseClasses.BaseModel
import com.google.gson.annotations.SerializedName


data class ClientListResponse(
    @SerializedName("status"  ) var status  : Int?            = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<ClientListData> = arrayListOf()

):BaseModel()

data class ClientListData(
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("lawyer_id"  ) var lawyerId  : Int?    = null,
    @SerializedName("name"       ) var name      : String? = null,
    @SerializedName("email"      ) var email     : String? = null,
    @SerializedName("mobile"     ) var mobile    : String? = null,
    @SerializedName("address"    ) var address   : String? = null,
    @SerializedName("image"    ) var image   : String? = null,
    @SerializedName("status"     ) var status    : Int?    = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null

):BaseModel()
