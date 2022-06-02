package ru.kirill.astro_app.viewmodel.marsPicture

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kirill.astro_app.repository.MarsPicture.DTO.MarsPictureResponseData
import ru.kirill.astro_app.repository.MarsPicture.MarsPictureAPIRetrofit2Impl
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayAPIRetrofit2Impl
import ru.kirill.astro_app.repository.PictureOfTheDay.PictureOfTheDayResponseData

class MarsPictureViewModel(
    private val liveData: MutableLiveData<MarsPictureAppState> = MutableLiveData(),
    private val marsPictureAPIRetrofit2Impl: MarsPictureAPIRetrofit2Impl = MarsPictureAPIRetrofit2Impl()
) : ViewModel() {

    fun getLiveData(): MutableLiveData<MarsPictureAppState> {
        return liveData
    }

    fun sendRequestCuriosity(date: String, camera: String) {
        liveData.postValue(MarsPictureAppState.Loading(null))
        // проверка на то есть ли BuildConfig.NASA_API_KEY
        marsPictureAPIRetrofit2Impl.getRetrofit().getPictureMarsCuriosity(ru.kirill.astro_app.BuildConfig.NASA_API_KEY, date,camera)
            .enqueue(callback)
    }

    fun sendRequestOpportunity(date: String, camera: String) {
        liveData.postValue(MarsPictureAppState.Loading(null))
        // проверка на то есть ли BuildConfig.NASA_API_KEY
        marsPictureAPIRetrofit2Impl.getRetrofit().getPictureMarsOpportunity(ru.kirill.astro_app.BuildConfig.NASA_API_KEY, date,camera)
            .enqueue(callback)
    }

    fun sendRequestSpirit(date: String, camera: String) {
        liveData.postValue(MarsPictureAppState.Loading(null))
        // проверка на то есть ли BuildConfig.NASA_API_KEY
        marsPictureAPIRetrofit2Impl.getRetrofit().getPictureMarsSpirit(ru.kirill.astro_app.BuildConfig.NASA_API_KEY, date,camera)
            .enqueue(callback)
    }

    private val callback = object : Callback<MarsPictureResponseData> {
        override fun onResponse(
            call: Call<MarsPictureResponseData>,
            response: Response<MarsPictureResponseData>
        ) {
            val responseCode = response.code()
            Log.d("@@@", responseCode.toString())
            if (response.isSuccessful) {
                response.body()?.let {
                    liveData.postValue(MarsPictureAppState.Success(it))
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

        override fun onFailure(call: Call<MarsPictureResponseData>, t: Throwable) {
            liveData.postValue(MarsPictureAppState.Error(Throwable()))
        }
    }
}
