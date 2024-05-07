package com.example.assignment_hard.type1

import com.google.gson.annotations.SerializedName

data class Image(val response: ImageDocument)

data class ImageDocument(
    @SerializedName("documents") val imageItem: MutableList<ImageItem>
)

data class ImageItem(
    val thumbnailUrl: String,
    val displaySitename: String,
    val datetime: String
)