package ru.kirill.astro_app.view.mars_picture

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentMarsPictureBinding
import ru.kirill.astro_app.viewmodel.marsPicture.MarsPictureAppState
import ru.kirill.astro_app.viewmodel.marsPicture.MarsPictureViewModel
import java.util.*


class MarsPictureFragment : Fragment() {

    private val KEY_SP_MARS = "sp_data_mars"
    private val KEY_ROVER_MARS = "current_rover_mars"
    private val KEY_CAMERA_MARS = "current_camera_mars"
    private val KEY_DATE_MARS = "current_date_mars"

    var roverName = arrayOf(
        "Curiosity", "Opportunity", "Spirit"
    )

    private fun convertRoverNameToIndex(rover: String): Int{
        return when (rover){
            roverName[0] -> 1
            roverName[1] -> 2
            else -> 3
        }
    }

    var camerasName = arrayOf(
        "FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM", "PANCAM", "MINITES"
    )

    private fun convertCameraNameToIndex(camera: String): Int{
        return when (camera){
            camerasName[0] -> 1
            camerasName[1] -> 2
            camerasName[2] -> 3
            camerasName[3] -> 4
            camerasName[4] -> 5
            camerasName[5] -> 6
            camerasName[6] -> 7
            camerasName[7] -> 8
            else -> 9
        }
    }

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
        binding.dateHack.dateMars.text = getDateMars()
        initAdapterRover()
        initAdapterCameras()
        choiceDate()
        searchPicture(choiceRover(), choiceCamera())
    }

    private fun searchPicture(rover: String, camera: String) {
        binding.searchPicture.setOnClickListener {
            when (rover) {
                roverName[0] -> {
                    initRequestCuriosity(binding.dateHack.dateMars.text.toString(), camera)
                    setDataMars(this.toString(), camera, binding.dateHack.dateMars.text.toString())
                }
                roverName[1] -> {
                    initRequestOpportunity(binding.dateHack.dateMars.text.toString(), camera)
                    setDataMars(this.toString(), camera, binding.dateHack.dateMars.text.toString())
                }
                roverName[2] -> {
                    initRequestSpirit(binding.dateHack.dateMars.toString(), camera)
                    setDataMars(this.toString(), camera, binding.dateHack.dateMars.text.toString())
                }
            }
        }
    }

    private fun initRequestCuriosity(date: String, camera: String) {
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it)
        })
        viewModel.sendRequestCuriosity(date, camera)
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
            is MarsPictureAppState.Error -> {
                Toast.makeText(
                    requireContext(),
                    marsPictureAppState.error.toString(),
                    Toast.LENGTH_SHORT
                )
            }
            is MarsPictureAppState.Loading -> {}
            is MarsPictureAppState.Success -> {
                Log.d("@@@", marsPictureAppState.marsPictureResponseData.photos.size.toString())
                if (marsPictureAppState.marsPictureResponseData.photos.isNotEmpty()) {

                    val picture = marsPictureAppState.marsPictureResponseData.photos.last().imgSrc
                    Log.d("@@@", "$picture")

                    binding.imageViewMars.load(uri = picture) {
                        placeholder(R.drawable.space)
                        error(R.drawable.ic_baseline_error_outline_24)
                    }
                } else {
                    binding.imageViewMars.load(R.drawable.mars) {
                        transformations(CircleCropTransformation())
                    }
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
        binding.spinnerCameras.setSelection(convertRoverNameToIndex(getRoverMars()))
    }

    @SuppressLint("ResourceType")
    private fun initAdapterCameras() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_cameras, R.id.cameras_names, camerasName
        )
        binding.spinnerCameras.adapter = adapter
        binding.spinnerCameras.setSelection(convertCameraNameToIndex(getCameraMars()))
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
                    val realMonth = monthOfYear + 1
                    binding.dateHack.dateMars.text = "$year-$realMonth-$dayOfMonth"
                }, yy, mm, dd
            )
            datePicker.show()
        }
    }

    private fun choiceRover(): String {
        return binding.spinnerRover.selectedItem.toString()
        Log.d("@@@", binding.spinnerRover.selectedItem.toString())
    }

    private fun choiceCamera(): String {
        return binding.spinnerCameras.selectedItem.toString()
        Log.d("@@@", binding.spinnerCameras.selectedItem.toString())
    }

    private fun setDataMars(rover: String, camera: String, date: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_MARS, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROVER_MARS, rover)
        editor.putString(KEY_CAMERA_MARS, camera)
        editor.putString(KEY_DATE_MARS, date)
        editor.apply()
    }

    private fun getRoverMars(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_MARS, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ROVER_MARS, "Curiosity").toString()
    }

    private fun getCameraMars(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_MARS, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_CAMERA_MARS, "FHAZ").toString()
    }

    private fun getDateMars(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_MARS, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_DATE_MARS, "DATE").toString()
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