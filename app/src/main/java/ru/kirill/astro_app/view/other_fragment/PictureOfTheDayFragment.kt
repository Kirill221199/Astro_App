package ru.kirill.astro_app.view.other_fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding
import ru.kirill.astro_app.view.MainActivity
import ru.kirill.astro_app.view.SettingsFragment
import ru.kirill.astro_app.view.WikiSearchFragment
import ru.kirill.astro_app.viewmodel.PictureOfTheDayAppState
import ru.kirill.astro_app.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!
    private var isMain = true
    lateinit var today:String

    private val customFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("uuuu-MM-d")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val yesterday = LocalDateTime.now().minusDays(2).format(customFormatter)!!
    @RequiresApi(Build.VERSION_CODES.O)
    val tdby = LocalDateTime.now().minusDays(3).format(customFormatter)!!


    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMargins(binding.tabLayoutMain, 0, binding.tvPicture.getWidthHeight().second, 0, 0)
        initActionBar()
        initRequest("")
        initBehaviorBottomSheet()
        animationFloatActionBar()
        setChip()
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    // Костыль чтобы узнать высоту элемента
    fun View.getWidthHeight():Pair<Int,Int>{
        measure(
            // horizontal space requirements as imposed by the parent
            0, // widthMeasureSpec

            // vertical space requirements as imposed by the parent
            0 // heightMeasureSpec
        )
        // the raw measured width of this view
        val width = measuredWidth
        // the raw measured height of this view
        val height = measuredHeight

        // return view's width and height in pixels
        return Pair(width,height)
    }

    private fun initActionBar() {
        (requireActivity() as MainActivity).setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }

    private fun initRequest(date: String) {
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequest(date)
    }


    private fun renderData(pictureOfTheDayAppState: PictureOfTheDayAppState) {
        when (pictureOfTheDayAppState) {
            is PictureOfTheDayAppState.Error -> {}
            is PictureOfTheDayAppState.Loading -> {}
            is PictureOfTheDayAppState.Success -> {
                binding.imageView.load(pictureOfTheDayAppState.pictureOfTheDayResponseData.url){
                    placeholder(R.drawable.space)
                    error(R.drawable.ic_baseline_error_outline_24)
                    transformations(CircleCropTransformation())
                }
                binding.lifeHack.title.text = pictureOfTheDayAppState.pictureOfTheDayResponseData.title
                binding.lifeHack.explanation.text = pictureOfTheDayAppState.pictureOfTheDayResponseData.explanation
                today = pictureOfTheDayAppState.pictureOfTheDayResponseData.date
                Log.d("@@@", "$today")
            }
        }
    }

    private fun initBehaviorBottomSheet(){
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.lifeHack.bottomSheetContainer)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            val color = 123456
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_COLLAPSED -> {}
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.tabLayoutMain.visibility = View.GONE
                        binding.tvPicture.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {
                        binding.tabLayoutMain.visibility = View.VISIBLE
                        binding.tvPicture.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("@@@", "$slideOffset")
            }

        })
    }

    private fun animationFloatActionBar() {
        binding.fab.setOnClickListener {
            if (isMain) {
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_back_fab
                    )
                )
            }
            else {
                binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_hamburger_menu_bottom_bar
                )
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
            isMain = !isMain
        }

    }

    private fun setChip() {
        binding.tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {initRequest("")}
                    1 -> {initRequest(yesterday)}
                    2 -> {initRequest(tdby)}
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                // что-то реализовать
                Log.d("@@@", "fav")
            }
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance()).addToBackStack("")
                    .commit()
            }
            R.id.app_bar_wiki -> {
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, WikiSearchFragment.newInstance()).addToBackStack("").commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "")
                // Нужно сделать скрытие по нажатию на элемент
            }
        }
        return super.onOptionsItemSelected(item)
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