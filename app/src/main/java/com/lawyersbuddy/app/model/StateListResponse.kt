package com.lawyersbuddy.app.model

data class StateListResponse(
    var `data`: ArrayList<StateListData>,
    var message: String,
    var status: Int
)

data class StateListData(
    var id: Int,
    var state: String
)