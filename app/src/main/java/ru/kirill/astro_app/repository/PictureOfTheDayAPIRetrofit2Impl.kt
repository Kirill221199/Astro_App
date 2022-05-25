package ru.kirill.astro_app.repository

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PictureOfTheDayAPIRetrofit2Impl {

    private val nasaBaseUrl = "https://api.nasa.gov/"

    fun getRetrofit(): PictureOfTheDayAPI{
        val PictureOfTheDayRetrofit = Retrofit.Builder()
            .baseUrl(nasaBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return PictureOfTheDayRetrofit.create(PictureOfTheDayAPI::class.java)
    }

}