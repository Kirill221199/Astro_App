package ru.kirill.astro_app.view.other_fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.kirill.astro_app.R
import ru.kirill.astro_app.databinding.FragmentWikiSearchBinding

class WikiSearchFragment : Fragment() {

    private var _binding: FragmentWikiSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWikiSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search()
        Glide.with(this).load(R.drawable.space).into(binding.imageViewWiki)
    }

    private fun search() {
        binding.inputLayoutWiki.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditTextWiki.text.toString()}")
            })
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            WikiSearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}