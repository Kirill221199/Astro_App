package ru.kirill.astro_app.view.picture

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentBottomNavigationDrawerBinding
import ru.kirill.astro_app.databinding.FragmentPictureOfTheDayBinding

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomNavigationDrawerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomNavigationDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_one ->{
                    Log.d("@@@","На экран 1")
                }
                R.id.navigation_two ->{
                    Log.d("@@@","На экран 2")
                }
            }
            true
        }

}

    companion object {
        @JvmStatic
        fun newInstance() =
            BottomNavigationDrawerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}