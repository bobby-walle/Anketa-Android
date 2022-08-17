package com.example.anketa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InstagramModel(
    val post: Int,
    val follower: Int,
    val following: Int,
): Parcelable