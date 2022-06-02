package ru.kirill.astro_app.view.mars_picture

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import androidx.lifecycle.Observer
import coil.transform.CircleCropTransformation
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentMarsPictureBinding
import ru.kirill.astro_app.viewmodel.marsPicture.MarsPictureAppState
import ru.kirill.astro_app.viewmodel.marsPicture.MarsPictureViewModel
import ru.kirill.astro_app.viewmodel.pictureOfTheDay.PictureOfTheDayAppState
import ru.kirill.astro_app.viewmodel.pictureOfTheDay.PictureOfTheDayViewModel
import java.util.*


class MarsPictureFragment : Fragment() {

    var roverName = arrayOf(
        "Curiosity", "Opportunity", "Spirit"
    )

    var camerasName = arrayOf(
        "FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM", "PANCAM", "MINITES"
    )

    private var _binding: FragmentMarsPictureBinding? = null
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
        _binding = FragmentMarsPictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: MarsPictureViewModel by lazy {
        ViewModelProvider(this).get(MarsPictureViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapterRover()
        initAdapterCameras()
        choiceDate()
        choiceRover()
        choiceCamera()
        initRequestCuriosity("2021-3-10","FHAZ")
    }

    private fun initRequestCuriosity(date: String, camera: String) {
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequestCuriosity(date,camera)
    }

    private fun initRequestOpportunity(date: String, camera: String) {
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequestOpportunity(date,camera)
    }

    private fun initRequestSpirit(date: String, camera: String) {
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequestSpirit(date,camera)
    }

    private fun renderData(marsPictureAppState: MarsPictureAppState) {
        when (marsPictureAppState) {
            is MarsPictureAppState.Error -> {}
            is MarsPictureAppState.Loading -> {}
            is MarsPictureAppState.Success -> {
                Log.d("@@@", marsPictureAppState.marsPictureResponseData.photos.size.toString())
                binding.imageViewMars.load(marsPictureAppState.marsPictureResponseData.photos.last().imgSrc){
                    placeholder(R.drawable.space)
                    error(R.drawable.ic_baseline_error_outline_24)
                    //transformations(CircleCropTransformation())
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun initAdapterRover() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_rover, R.id.rover_name, roverName
        )

        binding.spinnerRover.adapter = adapter
    }

    @SuppressLint("ResourceType")
    private fun initAdapterCameras() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_cameras, R.id.cameras_names, camerasName
        )

        binding.spinnerCameras.adapter = adapter
    }

    private fun choiceDate() {
        binding.dateHack.dateMars.setOnClickListener {
            val calendar = Calendar.getInstance()
            val yy = calendar[Calendar.YEAR]
            val mm = calendar[Calendar.MONTH]
            val dd = calendar[Calendar.DAY_OF_MONTH]
            val datePicker = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    calendar[year, monthOfYear] = dayOfMonth
                    binding.dateHack.dateMars.text = "$year-$monthOfYear-$dayOfMonth"
                }, yy, mm, dd
            )
            datePicker.show()
        }
    }

    private fun choiceRover() {
        binding.spinnerRover.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.RoverNames)
                val toast = Toast.makeText(
                    requireContext(),
                    "Your choice: " + choose[selectedItemPosition], Toast.LENGTH_SHORT
                )
                toast.show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun choiceCamera() {
        binding.spinnerCameras.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.CamerasNames)
                val toast = Toast.makeText(
                    requireContext(),
                    "Your choice: " + choose[selectedItemPosition], Toast.LENGTH_SHORT
                )
                toast.show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MarsPictureFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}