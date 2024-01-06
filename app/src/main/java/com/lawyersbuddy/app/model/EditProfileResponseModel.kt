package com.lawyersbuddy.app.model

import com.google.gson.annotations.SerializedName

data class EditProfileResponseModel (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : EditProfileData?   = EditProfileData()

)

data class EditProfileData (

    @SerializedName("id"                 ) var id               : Int?    = null,
    @SerializedName("lawyer_id"          ) var lawyerId         : Int?    = null,
    @SerializedName("type"               ) var type             : Int?    = null,
    @SerializedName("name"               ) var name             : String? = null,
    @SerializedName("firm_name"          ) var firmName         : String? = null,
    @SerializedName("email"              ) var email            : String? = null,
    @SerializedName("mobile"             ) var mobile           : String? = null,
    @SerializedName("email_verified_at"  ) var emailVerifiedAt  : String? = null,
    @SerializedName("city"               ) var city             : String? = null,
    @SerializedName("state"              ) var state            : String? = null,
    @SerializedName("zip_code"           ) var zipCode          : String? = null,
    @SerializedName("bar_association"    ) var barAssociation   : String? = null,
    @SerializedName("bar_council_number" ) var barCouncilNumber : String? = null,
    @SerializedName("address"            ) var address          : String? = null,
    @SerializedName("image"              ) var image            : String? = null,
    @SerializedName("status"             ) var status           : Int?    = null,
    @SerializedName("termcondition"      ) var termcondition    : String? = null,
    @SerializedName("is_permission"      ) var isPermission     : Int?    = null,
    @SerializedName("created_at"         ) var createdAt        : String? = null,
    @SerializedName("updated_at"         ) var updatedAt        : String? = null

)