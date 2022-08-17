package com.example.anketa.model

import android.os.Parcelable
import com.example.anketa.data.ResourceType

abstract class BaseModel : Parcelable {
    abstract val id: String
    abstract val name: String
    abstract val address: String
    abstract val position: String
    abstract val salary: String
    abstract val detailTxt: String
    abstract val rating: Float
    abstract val reviewsCount: Int
    abstract val arrayOfResources: Array<Pair<Int, ResourceType>>
    abstract val imgFriendsId: Int
    abstract val instagramModel: InstagramModel
    abstract val arrayOfComments: Array<CommentModel>
}
