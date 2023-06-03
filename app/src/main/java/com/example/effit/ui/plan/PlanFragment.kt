package com.example.effit.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.effit.R
import com.example.effit.data.model.Video
import com.example.effit.databinding.FragmentPlanBinding
import com.example.effit.util.hide
import com.example.effit.util.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class PlanFragment : Fragment() {
    private var _binding: FragmentPlanBinding? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private val videosCollection = FirebaseFirestore.getInstance().collection("videos")
    lateinit var video: Video

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlanBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = requireView().findViewById(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Обработка выбранного пункта меню "Home"
                    findNavController().navigate(R.id.navigation_home)
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

        arguments?.let {
            it.getParcelable<Video>("cardio")?.let { videoCardio ->
                if (videoCardio.title == "ACardio") {
                    val videoId = videoCardio.id
                    if (videoId != null) {
                        showVideo(
                            webView = binding.webViewCardio,
                            cardView = binding.cardView2,
                            videoId
                        )
                    }
                }
            }
        }

        arguments?.let {
            it.getParcelable<Video>("abs")?.let { video ->
                if (video.title == "CAbs") {
                    val videoId = video.id
                    if (videoId != null) {
                        showVideo(
                            webView = binding.webViewCardio,
                            cardView = binding.cardView2,
                            videoId
                        )
                    }
                }
            }
        }

        arguments?.let {
            it.getParcelable<Video>("buttocks")?.let { video ->
                if (video.title == "Buttocks") {
                    val videoId = video.id
                    if (videoId != null) {
                        showVideo(
                            webView = binding.webViewCardio,
                            cardView = binding.cardView2,
                            videoId
                        )
                    }
                }
            }
        }

        arguments?.let {
            it.getParcelable<Video>("upper")?.let { video ->
                if (video.title == "Upper") {
                    val videoId = video.id
                    if (videoId != null) {
                        showVideo(
                            webView = binding.webViewCardio,
                            cardView = binding.cardView2,
                            videoId
                        )
                    }
                }
            }
        }
    }

    private fun showVideo(webView: WebView, cardView: CardView, videoId: Int) {
        videosCollection
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[videoId]
                    val videoUrl = document.getString("url")
                     //Load the video URL in the WebView
                    webView.settings.javaScriptEnabled = true
                    if (videoUrl != null) {
                        binding.textView.hide()
                        binding.text.show()
                        webView.show()
                        cardView.show()
                        webView.loadUrl(videoUrl)
                    }
                    else {
                        binding.text.hide()
                        webView.hide()
                        cardView.hide()
                        binding.textView.show()
                    }
                }
            }
            .addOnFailureListener { exception ->

            }
    }
}