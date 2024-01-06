package com.lawyersbuddy.app.model

data class PaymentListResponse(
    var `data`: List<PaymentListData>,
    var message: String,
    var status: Int
)

data class PaymentListData(
    var amount: Int,
    var created_at: String,
    var id: Int,
    var lawyer_id: Int,
    var payment_by: String,
    var payment_date: String,
    var payment_time: String,
    var subscription_id: String,
    var subscription_name: String,
    var updated_at: String
)