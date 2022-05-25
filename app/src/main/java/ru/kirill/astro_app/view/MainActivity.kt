package ru.kirill.astro_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kirill.astro_app.R
import ru.kirill.astro_app.view.picture.PictureOfTheDayFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance()).commit()
        }
    }
}