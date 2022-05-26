package ru.kirill.astro_app.view.picture

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding
import ru.kirill.astro_app.view.MainActivity
import ru.kirill.astro_app.view.WikiSearchFragment
import ru.kirill.astro_app.viewmodel.PictureOfTheDayAppState
import ru.kirill.astro_app.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


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
        initActionBar()
        initRequest("")
        initBehaviorBottomSheet()
        animationFloatActionBar()
        setChip()
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
                    error(R.drawable.ic_archive)
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
                    BottomSheetBehavior.STATE_EXPANDED -> {binding.lifeHack.bottomSheetContainer.setBackgroundResource(R.drawable.space_background)}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {binding.lifeHack.bottomSheetContainer.setBackgroundColor(R.color.transparent!!)}
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
        binding.chipGroup.setOnCheckedChangeListener { group, position ->
            when (position) {
                1 -> {
                    initRequest("")
                }
                2 -> {
                    initRequest(yesterday)
                    Log.d("@@@", "$yesterday")
                }
                3 -> {
                    initRequest(tdby)
                    Log.d("@@@", "$tdby")
                }
            }
        }
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
                // что-то реализовать
                Log.d("@@@", "settings")
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