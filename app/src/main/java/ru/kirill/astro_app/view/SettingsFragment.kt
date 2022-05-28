package ru.kirill.astro_app.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.tabs.TabLayout
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTab()
    }

    private fun initTab(){
        binding.tabLayoutSettings.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {binding.imageView.load(R.drawable.earth){
                        transformations(CircleCropTransformation())}}
                    1 -> {binding.imageView.load(R.drawable.moon){
                        transformations(CircleCropTransformation())}}
                    2 -> {binding.imageView.load(R.drawable.mars){
                        transformations(CircleCropTransformation())
                    }}
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //что-то реализовать
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //что-то реализовать
            }
        })
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