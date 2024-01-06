package com.lawyersbuddy.app.model

data class SearchResponseListModelClass(
    var `data`: List<SearchResponsedataData>,
    var message: String,
    var status: Int
)

data class SearchResponsedataData(
    var address: String,
    var created_at: String,
    var email: String,
    var id: Int,
    var image: Any,
    var lawyer_id: Int,
    var mobile: String,
    var name: String,
    var status: Int,
    var updated_at: String
)