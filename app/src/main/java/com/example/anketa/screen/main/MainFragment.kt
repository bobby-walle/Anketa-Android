package com.example.anketa.screen.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.R
import com.example.anketa.databinding.FragmentMainBinding
import com.example.anketa.model.BaseModel
import com.example.anketa.model.EmployeeModel
import com.example.anketa.model.EmployerModel
import com.example.anketa.model.TwoCardModel
import com.example.anketa.prefs
import com.example.anketa.screen.RvLikeViewPagerAdapter
import com.example.anketa.screen.NavBarCallbacks
import com.example.anketa.screen.tools.OnSnapPositionChangeListener
import com.example.anketa.screen.tools.initLikeViewPager
import com.example.anketa.viewmodel.CardsViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var callbacks: NavBarCallbacks? = null
    val viewModel: CardsViewModel by activityViewModels()
    private lateinit var adapter: RvLikeViewPagerAdapter

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.apply {
            initUI(this)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel
            .stream
            .observe(viewLifecycleOwner) {
                bindCards(it)
            }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(binding: FragmentMainBinding) {
        with(binding) {
            initAdapters(this)
            setOnTransitionCompleted(this)
            bindTopCard(viewModel.topCard)
            bindBottomCard(viewModel.bottomCard)

            cardButtons.apply {
                btnLike.setOnClickListener {
                    onBtnLikeClick(viewModel.topCard.id)
                    motionLayout.transitionToState(R.id.offScreenLike)
                }

                btnDislike.setOnClickListener {
                    motionLayout.transitionToState(R.id.offScreenUnlike)
                }

                btnDetail.setOnClickListener {
                    onBtnDetailClick(viewModel.topCard)
                }
            }

            val toast = Toast.makeText(
                requireContext(),
                "Функционал еще в разработке",
                Toast.LENGTH_SHORT
            )

            btnFilters.setOnClickListener {
                toast.show()
            }

            searchPanel.setOnClickListener { toast.show() }


            btnList.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionToList())
            }
        }
    }

    private fun setOnTransitionCompleted(binding: FragmentMainBinding) {
        with(binding) {
            motionLayout.setTransitionListener(object : TransitionAdapter() {
                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    when (currentId) {
                        R.id.offScreenLike -> {
                            motionLayout.progress = 0f
                            motionLayout.setTransition(R.id.startMain, R.id.startMain)
                            onBtnLikeClick(viewModel.topCard.id)
                            viewModel.swipe()
                        }
                        R.id.offScreenUnlike -> {
                            motionLayout.progress = 0f
                            motionLayout.setTransition(R.id.startMain, R.id.startMain)
                            viewModel.swipe()
                        }
                    }
                }
            })
        }
    }

    private fun initAdapters(binding: FragmentMainBinding) {
        with(binding) {
            adapter =
                RvLikeViewPagerAdapter(
                    viewModel.topCard.arrayOfResources,
                    requireContext().packageName
                )
            rvLikeViewpager.adapter = adapter

            val layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            for (el in viewModel.topCard.arrayOfResources) {
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

    private fun onBtnDetailClick(model: BaseModel) {
        findNavController().navigate(
            MainFragmentDirections.actionToDetail(findNavController().currentDestination?.id!!, false, null, model)
        )
    }

    private fun onBtnLikeClick(id: String) {
        prefs.addResponse(id, viewModel.list.size)
    }

    private fun bindCards(model: TwoCardModel) {
        coroutineScope.launch {
            updateAdaptersBeforeSwipe(model)
            delay(10)
            bindBottomCard(model.cardBottom)
        }
    }

    private fun updateAdaptersBeforeSwipe(model: TwoCardModel) {
        with(binding) {

            tablayout.removeAllTabs()
            for (el in model.cardTop.arrayOfResources) {
                tablayout.addTab(tablayout.newTab())
            }

            adapter =
                RvLikeViewPagerAdapter(model.cardTop.arrayOfResources, requireContext().packageName)
            rvLikeViewpager.adapter = adapter
            bindTopCard(model.cardTop)
        }
    }

    private fun bindTopCard(cardTop: BaseModel) {
        with(binding) {
            when (cardTop) {
                is EmployeeModel -> {
                    View.GONE.let {
                        employerImgRatingFriends.visibility = it
                        employerTitleFriends.visibility = it
                        employerDataFriends.visibility = it
                    }

                    employeeCard.apply {
                        employeeCard.root.visibility = View.VISIBLE
                        employerCard.root.visibility = View.GONE
                        position.text = cardTop.position
                        name.text = cardTop.name
                        dataCity.text = cardTop.address
                        dataSalary.text = cardTop.salary
                        dataExperience.text = cardTop.experience
                        dataReviews.text = cardTop.reviewsCount.toString()
                        txtRatingProfile.text = cardTop.rating.toString()
                    }
                }
                is EmployerModel -> {
                    employerCard.root.visibility = View.VISIBLE
                    employeeCard.root.visibility = View.GONE
                    employerDataFriends.text = cardTop.instagramModel.follower.toString()
                    employerImgRatingFriends.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            cardTop.imgFriendsId,
                            null
                        )
                    )
                    employerCard.apply {
                        name.text = cardTop.name
                        address.text = cardTop.address
                        position.text = cardTop.position
                        dataSalary.text = cardTop.salary
                        dataReviews.text = cardTop.reviewsCount.toString()
                        txtRatingProfile.text = cardTop.rating.toString()
                    }
                }
                else -> {}
            }
        }
    }

    private fun bindBottomCard(cardBottom: BaseModel) {
        with(binding) {
            imageCard2.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    cardBottom.arrayOfResources[0].first,
                    null
                )
            )

            when (cardBottom) {
                is EmployeeModel -> {
                    View.GONE.let {
                        employerImgRatingFriends2.visibility = it
                        employerTitleFriends2.visibility = it
                        employerDataFriends2.visibility = it
                    }
                    employeeCard2.root.visibility = View.VISIBLE
                    employerCard2.root.visibility = View.GONE
                    employeeCard2.apply {
                        txtRatingProfile2.text = cardBottom.rating.toString()
                        position.text = cardBottom.position
                        name.text = cardBottom.name
                        dataCity.text = cardBottom.address
                        dataSalary.text = cardBottom.salary
                        dataExperience.text = cardBottom.experience
                        dataReviews.text = cardBottom.reviewsCount.toString()
                    }
                }
                is EmployerModel -> {
                    employerCard2.root.visibility = View.VISIBLE
                    employeeCard2.root.visibility = View.GONE
                    employerDataFriends2.text = cardBottom.instagramModel.follower.toString()
                    employerImgRatingFriends2.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            cardBottom.imgFriendsId,
                            null
                        )
                    )
                    employerCard2.apply {
                        txtRatingProfile2.text = cardBottom.rating.toString()
                        name.text = cardBottom.name
                        address.text = cardBottom.address
                        position.text = cardBottom.position
                        dataSalary.text = cardBottom.salary
                        dataReviews.text = cardBottom.reviewsCount.toString()
                    }
                }
                else -> {}
            }
        }
    }
}