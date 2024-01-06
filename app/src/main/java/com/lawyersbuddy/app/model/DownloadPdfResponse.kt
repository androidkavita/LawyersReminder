package com.lawyersbuddy.app.model

data class DownloadPdfResponse(
    var `data`: DownloadPdfData,
    var message: String,
    var status: Int
)

data class DownloadPdfData(
    var file_path: String
)