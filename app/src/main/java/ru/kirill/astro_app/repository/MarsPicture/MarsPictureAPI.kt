package ru.kirill.astro_app.repository.MarsPicture

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kirill.astro_app.repository.MarsPicture.DTO.MarsPictureResponseData
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayResponseData

interface MarsPictureAPI {

    //Fulfills its mission to this day
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getPictureMarsCuriosity(@Query("api_key") apiKey:String, @Query("earth_date", ) date:String, @Query("camera") camera:String,): Call<MarsPictureResponseData>

    //lost in a dust storm in 2019
    @GET("mars-photos/api/v1/rovers/opportunity/photos")
    fun getPictureMarsOpportunity(@Query("api_key") apiKey:String, @Query("earth_date", ) date:String, @Query("camera") camera:String,): Call<MarsPictureResponseData>

    //Stuck in the sands in 2010
    @GET("mars-photos/api/v1/rovers/spirit/photos")
    fun getPictureMarsSpirit(@Query("api_key") apiKey:String, @Query("earth_date", ) date:String, @Query("camera") camera:String,): Call<MarsPictureResponseData>
}