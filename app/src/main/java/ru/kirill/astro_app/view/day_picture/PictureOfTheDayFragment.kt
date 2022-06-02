package ru.kirill.astro_app.view.day_picture

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding


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