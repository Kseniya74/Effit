package com.example.effit.data.repository.video

import com.example.effit.data.model.Video
import com.google.firebase.firestore.FirebaseFirestore

class VideoRepositoryImpl: VideoRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val videosCollection = firestore.collection("videos/")

    override fun saveVideo(video: Video) {
        videosCollection.document("/${video.title}").set(video)
    }
}