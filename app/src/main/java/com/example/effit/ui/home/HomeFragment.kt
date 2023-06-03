package com.example.effit.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effit.R
import com.example.effit.data.model.Video
import com.example.effit.data.repository.video.VideoRepositoryImpl
import com.example.effit.databinding.FragmentHomeBinding
import com.example.effit.ui.auth.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var webViewCardio: WebView
    private lateinit var webViewButtocks: WebView
    private lateinit var webViewAbs: WebView
    private lateinit var webViewUpper: WebView
    private val videoCardio = "https://www.youtube.com/embed/68oIxSIwbyY"
    private val videoButtocks = "https://www.youtube.com/embed/y-nbg7vNkcY"
    private val videoAbs = "https://www.youtube.com/embed/Pd9Rgv07CH8"
    private val videoUpper = "https://www.youtube.com/embed/JTfT5dYLTiM"
    private val videoViewModel: VideoViewModel by viewModels()
    val bundle = Bundle()

    val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            authViewModel.logout {
                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            }
        }

        bottomNavigationView = requireView().findViewById(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Обработка выбранного пункта меню "Home"
                    findNavController().navigate(R.id.navigation_home)
                    binding.navView.setBackgroundColor(7)
                    true
                }

                R.id.navigation_plan -> {
                    // Обработка выбранного пункта меню "Search"
                    findNavController().navigate(R.id.navigation_plan)
                    true
                }

                R.id.navigation_profile -> {
                    // Обработка выбранного пункта меню "Profile"
                    findNavController().navigate(R.id.navigation_profile)
                    true
                }

                R.id.noteListingFragment -> {
                    findNavController().navigate(R.id.noteListingFragment)
                    true
                }

                else -> false
            }
        }
        webViewCardio = binding.webViewCardio
        webViewButtocks = binding.webViewButtocks
        webViewAbs = binding.webViewAbs
        webViewUpper = binding.webViewUpper
        loadVideo(videoCardio, webViewCardio)
        loadVideo(videoButtocks,webViewButtocks)
        loadVideo(videoAbs, webViewAbs)
        loadVideo(videoUpper, webViewUpper)

        binding.addCardio.setOnClickListener {
                val videoCardioSaved = getVideoObj(videoCardio, "ACardio", 0)
                videoViewModel.saveVideo(videoCardioSaved)
                bundle.putParcelable("cardio", videoCardioSaved)
                findNavController().navigate(R.id.action_navigation_home_to_navigation_plan, bundle)
                //sendVideo(videoCardioSaved.title)
        }
        binding.addButtocks.setOnClickListener {
            val videoButtocksSaved = getVideoObj(videoButtocks, "Buttocks", 1)
            videoViewModel.saveVideo(videoButtocksSaved)
            bundle.putParcelable("buttocks", videoButtocksSaved)
            findNavController().navigate(R.id.action_navigation_home_to_navigation_plan, bundle)
            //sendVideo(videoAbsSaved.title)
        }
        binding.addAbs.setOnClickListener {
            val videoAbsSaved = getVideoObj(videoAbs, "CAbs", 2)
            videoViewModel.saveVideo(videoAbsSaved)
            bundle.putParcelable("abs", videoAbsSaved)
            findNavController().navigate(R.id.action_navigation_home_to_navigation_plan, bundle)
            //sendVideo(videoButtocksSaved.title)
        }
        binding.addUpper.setOnClickListener {
            val videoUpperSaved = getVideoObj(videoUpper, "Upper", 3)
            videoViewModel.saveVideo(videoUpperSaved)
            bundle.putParcelable("upper", videoUpperSaved)
            findNavController().navigate(R.id.action_navigation_home_to_navigation_plan, bundle)
            //sendVideo(videoUpperSaved.title)
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadVideo(video: String, webView: WebView) {
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.mediaPlaybackRequiresUserGesture = false
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webView.loadData(
            "<html><body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"$video\" frameborder=\"0\" allowfullscreen></iframe></body></html>",
            "text/html",
            "utf-8"
        )
    }

    private fun getVideoObj(url: String, title: String, id: Int): Video {
        return Video(
            title = title,
            url = url,
            id = id
        )
    }
}