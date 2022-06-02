package ru.kirill.astro_app.view.day_picture

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding
import ru.kirill.astro_app.databinding.FragmentPictureTdbyBinding
import ru.kirill.astro_app.viewmodel.PictureOfTheDayAppState
import ru.kirill.astro_app.viewmodel.PictureOfTheDayViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PictureTdbyFragment : Fragment() {

    private var _binding: FragmentPictureTdbyBinding? = null
    private val binding get() = _binding!!

    private val customFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("uuuu-MM-d")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val tdby = LocalDateTime.now().minusDays(3).format(customFormatter)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPictureTdbyBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRequest(tdby)
        initBehaviorBottomSheet()
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
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("@@@", "$slideOffset")
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PictureTdbyFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}