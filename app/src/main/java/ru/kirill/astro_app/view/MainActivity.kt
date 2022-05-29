package ru.kirill.astro_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kirill.astro_app.R
import ru.kirill.astro_app.view.picture.PictureOfTheDayFragment

const val ThemeEarth = 0
const val ThemeMoon = 1
const val ThemeMars = 2

class MainActivity : AppCompatActivity() {

    private val KEY_SP = "sp"
    private val KEY_THEME = "current_theme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance()).commit()
        }
    }

    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_THEME, currentTheme)
        editor.apply()
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_THEME, -1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            ThemeEarth -> R.style.Earth
            ThemeMoon -> R.style.Moon
            ThemeMars -> R.style.Mars
            else -> 0
        }
    }
}
