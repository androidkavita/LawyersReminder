package com.lawyersbuddy.app.model


import com.google.gson.annotations.SerializedName


data class LoginOtpResponseModel (

    @SerializedName("status"  ) var status  : Int?    = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var loginResponseData    : LoginOtpData?   = LoginOtpData()

)


data class LoginOtpData (

    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("role"              ) var role            : Int?    = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("mobile_number"     ) var mobileNumber    : String? = null,
    @SerializedName("address"           ) var address         : String? = null,
    @SerializedName("profile_image"     ) var profileImage    : String? = null,
    @SerializedName("email_verified_at" ) var emailVerifiedAt : String? = null,
    @SerializedName("device_token"      ) var deviceToken     : String? = null,
    @SerializedName("device_name"       ) var deviceName      : String? = null,
    @SerializedName("device_type"       ) var deviceType      : String? = null,
    @SerializedName("device_id"         ) var deviceId        : String? = null,
    @SerializedName("otp"               ) var otp             : String? = null,
    @SerializedName("status"            ) var status          : Int?    = null,
    @SerializedName("complete_profile"  ) var completeProfile : Int?    = null,
    @SerializedName("api_token"         ) var apiToken        : String? = null,
    @SerializedName("amount"            ) var amount          : String? = null,
    @SerializedName("is_deleted"        ) var isDeleted       : Int?    = null,
    @SerializedName("is_approved"       ) var isApproved      : Int?    = null,
    @SerializedName("last_logedin_date" ) var lastLogedinDate : String? = null,
    @SerializedName("login_status"      ) var loginStatus     : Int?    = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null,
    @SerializedName("whatsapp_status"   ) var whatsappStatus  : String? = null,
    @SerializedName("sms_email"         ) var smsEmail       : String? = null

)