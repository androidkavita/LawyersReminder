package com.lawyersbuddy.app.model

data class CityListResponse(
    var `data`: ArrayList<CityListData>,
    var message: String,
    var status: Int
)

data class CityListData(
    var city: String,
    var id: Int,
    var state_id: Int
)