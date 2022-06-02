package ru.kirill.astro_app.repository.PictureOfTheDay

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayResponseData

interface PictureOfTheDayAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey:String): Call<PictureOfTheDayResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayWithDate(@Query("api_key") apiKey:String, @Query("date") date:String): Call<PictureOfTheDayResponseData>
}