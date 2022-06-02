package ru.kirill.astro_app.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentMainBinding
import ru.kirill.astro_app.view.other_fragment.SettingsFragment
import ru.kirill.astro_app.view.other_fragment.WikiSearchFragment
import ru.kirill.astro_app.view.day_picture.PictureOfTheDayFragment
import ru.kirill.astro_app.view.earth_picture.EarthPictureFragment
import ru.kirill.astro_app.view.mars_picture.MarsPictureFragment

class MainFragment : Fragment() {

    private val KEY_SP = "sp"
    private val KEY_ITEM = "current_item"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigation()
    }

    private fun bottomNavigation() {
        binding.bottomNavigationView.inflateMenu(R.menu.menu_bottom_bar)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_bottom_photo_day -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container_to_fragment, PictureOfTheDayFragment.newInstance())
                        .commit()
                    setCurrentItem(R.id.action_bottom_photo_day)
                }
                R.id.action_bottom_photo_earth -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container_to_fragment, EarthPictureFragment.newInstance())
                        .commit()
                    setCurrentItem(R.id.action_bottom_photo_earth)
                }
                R.id.action_bottom_mars_rover_photo -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container_to_fragment, MarsPictureFragment.newInstance())
                        .commit()
                    setCurrentItem(R.id.action_bottom_mars_rover_photo)
                }
                R.id.action_bar_wiki -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container_to_fragment, WikiSearchFragment.newInstance())
                        .commit()
                    setCurrentItem(R.id.action_bar_wiki)
                }
                R.id.action_bar_settings -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container_to_fragment, SettingsFragment.newInstance())
                        .commit()
                    setCurrentItem(R.id.action_bar_settings)
                }
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = getCurrentItem()
    }

    private fun setCurrentItem(currentItem: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences(KEY_SP, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_ITEM, currentItem)
        editor.apply()
    }

    private fun getCurrentItem(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences(KEY_SP, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_ITEM, R.id.action_bottom_photo_day)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}