package ru.kirill.astro_app.viewmodel

import ru.kirill.astro_app.repository.PictureOfTheDayResponseData

sealed class PictureOfTheDayAppState{
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData):PictureOfTheDayAppState()
    data class Error(val error:Throwable):PictureOfTheDayAppState()
    data class Loading(val progress:Int?):PictureOfTheDayAppState()
}