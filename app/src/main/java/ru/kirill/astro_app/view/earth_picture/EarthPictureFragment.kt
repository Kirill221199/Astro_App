package ru.kirill.astro_app.view.earth_picture

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.kirill.astro_app.R

class EarthPictureFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_earth_picture, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EarthPictureFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}