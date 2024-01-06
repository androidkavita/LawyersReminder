package com.lawyersbuddy.app.model

import com.lawyersbuddy.app.baseClasses.BaseModel

data class AssistantListResponse(
    var `data`: List<AssistantListData>,
    var message: String,
    var status: Int
): BaseModel()

data class AssistantListData(
    var email: String,
    var id: Int,
    var is_permission: Int,
    var mobile: String,
    var name: String,
    var image: String,
):BaseModel()