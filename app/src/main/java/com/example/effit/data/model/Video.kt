package com.example.effit.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    var id: Int? = null,
    val url: String = "",
    val title: String = ""
) : Parcelable
