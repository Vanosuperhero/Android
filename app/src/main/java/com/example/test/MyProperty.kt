package com.example.test

import com.squareup.moshi.Json

data class MyProperty (
        val description: String,
        @Json(name = "gifURL") val imgSrcUrl: String,
    )
