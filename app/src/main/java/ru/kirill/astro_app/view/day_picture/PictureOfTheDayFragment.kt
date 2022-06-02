package ru.kirill.astro_app.view.day_picture

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding
import ru.kirill.astro_app.viewmodel.PictureOfTheDayAppState
import ru.kirill.astro_app.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPagerPictureOfTheDay.adapter = AstroViewPager2Adapter(this)
        setTab()
    }


    private fun setTab(){
        TabLayoutMediator(binding.tabLayoutMain,binding.viewPagerPictureOfTheDay,object :TabLayoutMediator.TabConfigurationStrategy{
            override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                tab.text = when(position){
                    0-> "today"
                    1-> "yesterday"
                    else -> "tdby"
                }
            }
        } ).attach()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PictureOfTheDayFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}