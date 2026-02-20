package com.securetech.wallpapers.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.securetech.wallpapers.BuildConfig
import com.securetech.wallpapers.R
import com.securetech.wallpapers.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewVersion.text = getString(R.string.about_version, BuildConfig.VERSION_NAME)

        binding.buttonVisitPixabay.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.pixabay_url)))
            startActivity(intent)
        }

        binding.buttonPrivacyPolicy.setOnClickListener {
            findNavController().navigate(R.id.action_about_to_privacyPolicy)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
