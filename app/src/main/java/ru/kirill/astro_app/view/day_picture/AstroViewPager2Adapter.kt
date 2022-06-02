package ru.kirill.astro_app.view.day_picture

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AstroViewPager2Adapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val today = PictureTodayFragment
    private val yesterday = PictureYesterdayFragment
    private val tdby = PictureTdbyFragment
    private val fragments = arrayOf(today,yesterday,tdby)

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> today as Fragment
            1-> yesterday as Fragment
            else -> tdby as Fragment
        }
    }
}
