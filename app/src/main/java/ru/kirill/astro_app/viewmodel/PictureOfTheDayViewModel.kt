package ru.kirill.astro_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.BuildConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kirill.astro_app.BuildConfig.NASA_API_KEY
import ru.kirill.astro_app.repository.PictureOfTheDayAPIRetrofit2Impl
import ru.kirill.astro_app.repository.PictureOfTheDayResponseData

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData(),
    private val pictureOfTheDayRetrofit2Impl: PictureOfTheDayAPIRetrofit2Impl = PictureOfTheDayAPIRetrofit2Impl()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<PictureOfTheDayAppState> {
        return liveData
    }

    fun sendRequest() {
        liveData.postValue(PictureOfTheDayAppState.Loading(null))
        // проверка на то есть ли BuildConfig.NASA_API_KEY
        pictureOfTheDayRetrofit2Impl.getRetrofit().getPictureOfTheDay(ru.kirill.astro_app.BuildConfig.NASA_API_KEY)
            .enqueue(callback)
    }

    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(PictureOfTheDayAppState.Success(it))
                }
            } else {
                // доделать что-то на ошибку
            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            // доделать что-то на ошибку
        }
    }
}
