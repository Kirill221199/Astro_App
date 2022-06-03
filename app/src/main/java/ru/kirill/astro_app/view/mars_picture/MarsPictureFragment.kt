package ru.kirill.astro_app.view.mars_picture

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
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


    //region SharedPreferences to date, rover, camera
    private val KEY_SP_CAMERA = "sp_camera"
    private val KEY_CAMERA = "current_camera"

    private fun setCamera(camera: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_CAMERA, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_CAMERA, camera)
        editor.apply()
    }

    private fun getCamera(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_CAMERA, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_CAMERA, "FHAZ").toString()
    }

    private val KEY_SP_ROVER = "sp_rover"
    private val KEY_ROVER = "current_rover"

    private fun setRover(rover: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_ROVER, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROVER, rover)
        editor.apply()
    }

    private fun getRover(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_ROVER, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_ROVER, "Curiosity").toString()
    }

    private val KEY_SP_DATE = "sp_date"
    private val KEY_DATE = "current_date"

    private fun setDate(date: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_DATE, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_DATE, date)
        editor.apply()
    }

    private fun getDate(): String {
        val sharedPreferences =
            requireActivity().getSharedPreferences(KEY_SP_DATE, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_DATE, "").toString()
    }
    //endregion


    //region Spinner arrays and converter to position
    private var roverName = arrayOf(
        "Curiosity", "Opportunity", "Spirit"
    )

    private fun convertRoverNameToPosition(rover: String): Int{
        return when(rover){
            roverName[0] -> 0
            roverName[1] -> 1
            else -> 2
        }
    }

    private var camerasName = arrayOf(
        "FHAZ", "RHAZ", "MAST", "CHEMCAM", "MAHLI", "MARDI", "NAVCAM", "PANCAM", "MINITES"
    )

    private fun convertCamerasNameToPosition(camera: String): Int{
        return when(camera){
            camerasName[0] -> 0
            camerasName[1] -> 1
            camerasName[2] -> 2
            camerasName[3] -> 3
            camerasName[4] -> 4
            camerasName[5] -> 5
            camerasName[6] -> 6
            camerasName[7] -> 7
            else -> 8
        }
    }
    //endregion

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
        choiceDateCalendar()
        choiceRover()
        choiceCamera()
        searchPictureToCalendar(getRover(), getCamera())
        choiceDateText()
    }

    private fun searchPictureToCalendar(rover: String, camera: String) {
        binding.searchPicture.setOnClickListener {
            if (binding.dateHack.dateMars.text.toString() != "") {
                when (rover) {
                    roverName[0] -> {
                        initRequestCuriosity(binding.dateHack.dateMars.text.toString(), camera)
                    }
                    roverName[1] -> {
                        initRequestOpportunity(binding.dateHack.dateMars.text.toString(), camera)
                    }
                    roverName[2] -> {
                        initRequestSpirit(binding.dateHack.dateMars.toString(), camera)
                    }
                }
            }
            else{
                Toast.makeText(requireContext(),"SELECT DATE TO SEARCH", Toast.LENGTH_SHORT).show()
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
                ).show()
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

    //region Spinner and and their adapters
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

    private fun choiceRover() {
        binding.spinnerRover.setSelection(convertRoverNameToPosition(getRover()))
        binding.spinnerRover.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.RoverNames)
                val rover = choose[selectedItemPosition]
                setRover(rover)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun choiceCamera() {
        binding.spinnerCameras.setSelection(convertCamerasNameToPosition(getCamera()))
        binding.spinnerCameras.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.CamerasNames)
                val camera = choose[selectedItemPosition]
                setCamera(camera)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }
    //endregion

    private fun choiceDateCalendar() {
        binding.dateHack.dateMars.text = getDate()
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
                    val date = "$year-$realMonth-$dayOfMonth"
                    binding.dateHack.dateMars.text = date
                    setDate(date)
                }, yy, mm, dd
            )
            datePicker.show()
        }
    }

    private fun choiceDateText() {
        binding.dateHack.dateMars.setOnLongClickListener {
            showDialog()
            return@setOnLongClickListener true
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.date_dialog)
        val body = dialog.findViewById(R.id.body) as EditText
        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
        val noBtn = dialog.findViewById(R.id.noBtn) as Button
        yesBtn.setOnClickListener {
            val date = body.text.toString()
            binding.dateHack.dateMars.text = date
            setDate(date)
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

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