package ru.kirill.astro_app.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayout
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private val KEY_SP_SETTINGS = "sp_settings"
    private val KEY_THEME_SETTINGS = "current_theme_settings"

    private lateinit var parentActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = (context as MainActivity)
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStartSet()
        initTab()
        setDefaultTheme()
        setChooseTheme()
    }

    private fun initTab(){
        binding.tabLayoutSettings.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> { binding.imageView.load(R.drawable.earth) {
                        transformations(CircleCropTransformation()) }
                    }
                    1 -> { binding.imageView.load(R.drawable.moon) {
                        transformations(CircleCropTransformation()) }
                    }
                    2 -> { binding.imageView.load(R.drawable.mars) {
                        transformations(CircleCropTransformation()) }
                    }
                }
                setPositionTheme(tab!!.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //что-то реализовать
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //что-то реализовать
            }
        })
    }

    private fun setChooseTheme() {
        binding.buttonSettingsChoose.setOnClickListener {
            parentActivity.setCurrentTheme(getPositionTheme())
            parentActivity.recreate()
        }
    }

    private fun setDefaultTheme() {
        binding.buttonSettingsDefault.setOnClickListener {
            parentActivity.setCurrentTheme(R.style.Theme_Astro_App)
            parentActivity.recreate()
        }
    }

    private fun setPositionTheme(positionTheme: Int) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_SETTINGS, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_THEME_SETTINGS, positionTheme)
        editor.apply()
    }

    private fun getPositionTheme(): Int {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_SETTINGS, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_THEME_SETTINGS, -1)
    }

    private fun setStartSet(){
        val tab = binding.tabLayoutSettings.getTabAt(getPositionTheme())
        tab?.select()
        when(getPositionTheme()){
            0 -> binding.imageView.load(R.drawable.earth) {
                transformations(CircleCropTransformation())
            }
            1 -> binding.imageView.load(R.drawable.moon) {
                transformations(CircleCropTransformation())
            }
            2 -> binding.imageView.load(R.drawable.mars) {
                transformations(CircleCropTransformation())
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}