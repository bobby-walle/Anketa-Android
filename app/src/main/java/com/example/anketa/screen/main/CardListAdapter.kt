package com.example.anketa.screen.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.data.ResourceType
import com.example.anketa.databinding.ItemCardBinding
import com.example.anketa.model.BaseModel
import com.example.anketa.model.EmployeeModel
import com.example.anketa.model.EmployerModel
import com.example.anketa.screen.RvLikeViewPagerAdapter
import com.example.anketa.screen.tools.OnSnapPositionChangeListener
import com.example.anketa.screen.tools.initLikeViewPager

class CardListAdapter(
    val context: Context,
    list: MutableList<BaseModel>,
    private val onBtnDetailClicked: (BaseModel) -> Unit,
    private val onBtnLikeClicked: (String) -> Unit
) :
    RecyclerView.Adapter<CardListAdapter.ViewHolder>() {

    inner class ViewHolder(
        val binding: ItemCardBinding,
        val onBtnDetailClicked: (BaseModel) -> Unit,
        val onBtnLikeClicked: (String) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun setItem(model: BaseModel) {
            with(binding) {
                btnDetail.setOnClickListener {
                    onBtnDetailClicked(model)
                }
                btnLike.setOnClickListener {
                    onBtnLikeClicked(model.id)
                }
            }
        }
    }

    var modelsList = list
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(inflater, parent, false)
        return ViewHolder(
            binding,
            { model -> onBtnDetailClicked(model) },
            { id -> onBtnLikeClicked(id) }
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val model = modelsList[index]

        with(holder.binding) {
            initRecyclerViewLikeViewPager(this, model.arrayOfResources)

            when (model) {
                is EmployeeModel -> {
                    employeeCard.apply {
                        View.GONE.let {
                            employerImgRatingFriends.visibility = it
                            employerDataFriends.visibility = it
                            employerTitleFriends.visibility = it
                            employerCard.root.visibility = it
                        }
                        employeeCard.root.visibility = View.VISIBLE
                        position.text = model.position
                        name.text = model.name
                        dataCity.text = model.address
                        dataSalary.text = model.salary
                        dataExperience.text = model.experience
                        dataReviews.text = model.reviewsCount.toString()
                        txtRatingProfile.text = model.rating.toString()
                    }
                }
                is EmployerModel -> {
                    employerDataFriends.text = model.instagramModel.follower.toString()
                    employerCard.root.visibility = View.VISIBLE
                    employeeCard.root.visibility = View.GONE
                    employerCard.apply {
                        employerImgRatingFriends.setImageDrawable(
                            ResourcesCompat.getDrawable(
                                holder.itemView.resources,
                                model.imgFriendsId,
                                null
                            )
                        )
                        name.text = model.name
                        address.text = model.address
                        position.text = model.position
                        dataSalary.text = model.salary
                        dataReviews.text = model.reviewsCount.toString()
                        txtRatingProfile.text = model.rating.toString()
                    }
                }
                else -> {}
            }
        }
        holder.setItem(model)
    }

    override fun getItemCount(): Int {
        return modelsList.size
    }

    private fun initRecyclerViewLikeViewPager(binding: ItemCardBinding, resources: Array<Pair<Int, ResourceType>>) {
        with(binding) {
            rvLikeViewpager.adapter =
                RvLikeViewPagerAdapter(resources, context.packageName)

            val layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            for (el in 1..resources.size) {
                tablayout.addTab(tablayout.newTab())
            }

            rvLikeViewpager.initLikeViewPager(
                layoutManager,
                object : OnSnapPositionChangeListener {
                    override fun onSnapPositionChange(position: Int) {
                        tablayout.getTabAt(position)?.select()
                    }
                })
        }
    }
}