package ru.kirill.astro_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayAPIRetrofit2Impl
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayResponseData

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureOfTheDayAppState> = MutableLiveData(),
    private val pictureOfTheDayRetrofit2Impl: PictureOfTheDayAPIRetrofit2Impl = PictureOfTheDayAPIRetrofit2Impl()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<PictureOfTheDayAppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(PictureOfTheDayAppState.Loading(null))
        // проверка на то есть ли BuildConfig.NASA_API_KEY
        pictureOfTheDayRetrofit2Impl.getRetrofit().getPictureOfTheDayWithDate(ru.kirill.astro_app.BuildConfig.NASA_API_KEY, date)
            .enqueue(callback)
    }

    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            val responseCode = response.code()
            Log.d("@@@", responseCode.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(PictureOfTheDayAppState.Success(it))
                }
            } else {
                val responseCode = response.code()
                Log.d("@@@", responseCode.toString())
                val codeErrorServer = 500
                val codeErrorClient = 400..499

                when {
                    responseCode >= codeErrorServer -> {
                        // server
                        Log.d("@@@", "server error")
                    }
                    responseCode in codeErrorClient -> {
                        // client
                        Log.d("@@@", "client error")
                    }
                }
            }

        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            liveData.postValue(PictureOfTheDayAppState.Error(Throwable()))
        }
    }
}
