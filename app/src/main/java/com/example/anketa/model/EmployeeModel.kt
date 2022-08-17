package com.example.anketa.model

import android.os.Parcelable
import com.example.anketa.data.ResourceType
import kotlinx.parcelize.Parcelize

@Parcelize
class EmployeeModel(
    override val id: String,
    override val name: String,
    override val address: String,
    override val position: String,
    override val salary: String,
    override val rating: Float,
    override val detailTxt: String,
    override val reviewsCount: Int,
    override val arrayOfResources: Array<Pair<Int, ResourceType>>,
    override val imgFriendsId: Int,
    override val instagramModel: InstagramModel,
    override val arrayOfComments: Array<CommentModel>,
    val experience: String,
) : BaseModel(), Parcelable