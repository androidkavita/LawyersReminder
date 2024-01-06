package com.lawyersbuddy.app.model

data class NotificationResponse(
    var `data`: List<NotificationData>,
    var message: String,
    var status: Int
)

data class NotificationData(
    var created_at: String,
    var id: Int,
    var message: String,
    var status: Int,
    var title: String,
    var updated_at: String,
    var user_id: Int
)