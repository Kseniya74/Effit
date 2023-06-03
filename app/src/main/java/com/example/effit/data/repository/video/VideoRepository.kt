package com.example.effit.data.repository.video

import com.example.effit.data.model.Video

interface VideoRepository {
    fun saveVideo(video: Video)
}