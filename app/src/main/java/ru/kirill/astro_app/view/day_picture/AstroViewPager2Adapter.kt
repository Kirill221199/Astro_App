package ru.kirill.astro_app.view.day_picture

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kirill.astro_app.view.day_picture.days_fragment.PictureTdbyFragment
import ru.kirill.astro_app.view.day_picture.days_fragment.PictureTodayFragment
import ru.kirill.astro_app.view.day_picture.days_fragment.PictureYesterdayFragment

class AstroViewPager2Adapter(fa: Fragment) : FragmentStateAdapter(fa) {

    val today:Fragment = PictureTodayFragment.newInstance()
    val yesterday:Fragment = PictureYesterdayFragment.newInstance()
    val tdby:Fragment = PictureTdbyFragment.newInstance()
    private val fragments = arrayOf(today,yesterday,tdby)

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}
