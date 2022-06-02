package ru.kirill.astro_app.viewmodel.marsPicture

import ru.kirill.astro_app.repository.MarsPicture.DTO.MarsPictureResponseData

sealed class MarsPictureAppState {
    data class Success(val marsPictureResponseData: MarsPictureResponseData) :
        MarsPictureAppState()

    data class Error(val error: Throwable) : MarsPictureAppState()
    data class Loading(val progress: Int?) : MarsPictureAppState()
}