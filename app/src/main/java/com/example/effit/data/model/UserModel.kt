package com.example.effit.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var id: String = "",
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
): Parcelable