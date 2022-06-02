package ru.kirill.astro_app.repository.MarsPicture

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarsPictureAPIRetrofit2Impl {

    private val nasaBaseUrl = "https://api.nasa.gov/"

    fun getRetrofit(): MarsPictureAPI {
        val MarsPictureRetrofit = Retrofit.Builder()
            .baseUrl(nasaBaseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return MarsPictureRetrofit.create(MarsPictureAPI::class.java)
    }

}