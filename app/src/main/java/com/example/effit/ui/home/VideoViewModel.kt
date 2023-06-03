package com.example.effit.ui.home

import androidx.lifecycle.ViewModel
import com.example.effit.data.model.Video
import com.example.effit.data.repository.video.VideoRepositoryImpl

class VideoViewModel: ViewModel() {
    private val repository = VideoRepositoryImpl()

    fun saveVideo(video: Video) {
        repository.saveVideo(video)
    }

}