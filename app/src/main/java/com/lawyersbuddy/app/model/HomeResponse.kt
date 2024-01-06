package com.lawyersbuddy.app.model

data class HomeResponse(
    var `data`: HomeData,
    var message: String,
    var status: Int
)

data class HomeData(
    var assistant_count: Int,
    var banner_img: ArrayList<BannerImg>,
    var case_count: Int,
    var client_count: Int,
    var hearing_count: Int
)

data class BannerImg(
    var image: String
)