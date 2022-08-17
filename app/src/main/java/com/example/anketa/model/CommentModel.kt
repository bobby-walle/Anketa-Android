package com.example.anketa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CommentModel(
    val imgAvatarID: Int,
    val imgStarsID: Int,
    val name: String,
    val nickname: String,
    val text: String
) : Parcelable
