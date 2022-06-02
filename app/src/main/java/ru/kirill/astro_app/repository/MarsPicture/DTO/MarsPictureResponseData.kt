package ru.kirill.astro_app.repository.MarsPicture.DTO


import com.google.gson.annotations.SerializedName

data class MarsPictureResponseData(
    @SerializedName("photos")
    val photos: List<Photo>
)