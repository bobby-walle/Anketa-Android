package com.example.anketa.screen.detail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.R
import com.example.anketa.databinding.BtnLikeBinding
import com.example.anketa.databinding.FragmentDetailBinding
import com.example.anketa.databinding.ReviewLayoutBinding
import com.example.anketa.model.BaseModel
import com.example.anketa.model.EmployeeModel
import com.example.anketa.model.EmployerModel
import com.example.anketa.model.Vacancy
import com.example.anketa.prefs
import com.example.anketa.screen.RvLikeViewPagerAdapter
import com.example.anketa.screen.NavBarCallbacks
import com.example.anketa.screen.profile.employer.EditProfileEmployerFragmentDirections
import com.example.anketa.screen.tools.OnSnapPositionChangeListener
import com.example.anketa.screen.tools.initLikeViewPager
import com.example.anketa.viewmodel.CardsViewModel


class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private var navBarCallbacks: NavBarCallbacks? = null
    val viewModel: CardsViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navBarCallbacks = context as NavBarCallbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateBack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        navBarCallbacks?.hideNavBar()
        val model = args.model
        val vacancy = args.vacancy
        val isLiked = args.isLiked

        return binding.apply {
            initUI(this, container, model, vacancy, isLiked)
        }.root
    }

    override fun onDetach() {
        super.onDetach()
        navBarCallbacks = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(
        binding: FragmentDetailBinding,
        container: ViewGroup?,
        model: BaseModel,
        vacancy: Vacancy?,
        isLiked: Boolean,
    ) {
        bindAdapters(binding, model)
        bindProfileData(binding, model, vacancy)
        bindInstagramView(binding, model)
        bindReviewViews(binding, container, model)
        if ((!isLiked)) {
            bindLikeBtn(binding, container, model)
        }
        bindNavigation(binding)
    }

    private fun bindNavigation(binding: FragmentDetailBinding) {
        with(binding) {
            btnBack.setOnClickListener {
                navigateBack()
            }
        }
    }

    private fun bindAdapters(binding: FragmentDetailBinding, model: BaseModel) {
        with(binding) {
            rvLikeViewpager.adapter =
                RvLikeViewPagerAdapter(model.arrayOfResources, requireContext().packageName)

            val layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            for (el in model.arrayOfResources) {
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

    private fun bindProfileData(
        binding: FragmentDetailBinding,
        model: BaseModel,
        vacancy: Vacancy?,
    ) {
        with(binding) {
            txtRatingProfile.text = model.rating.toString()
            when (model) {
                is EmployeeModel -> {
                    View.GONE.let {
                        employerData.root.visibility = it
                        employerTxtDetail.visibility = it
                        employerBtnDetail.visibility = it
                        employerScales.root.visibility = it
                    }
                    View.VISIBLE.let {
                        employeeData.root.visibility = it
                        employeeTxtDetail.visibility = it
                    }
                    employeeTxtDetail.text = model.detailTxt

                    employeeData.position.text = model.position
                    employeeData.name.text = model.name
                    employeeData.dataSalary.text = model.salary
                    employeeData.dataExperience.text = model.experience
                    employeeData.titleReviews.visibility = View.GONE
                }
                is EmployerModel -> {
                    View.GONE.let {
                        employeeData.root.visibility = it
                        employeeTxtDetail.visibility = it
                    }
                    View.VISIBLE.let {
                        employerData.root.visibility = it
                        employerTxtDetail.visibility = it
                        employerBtnDetail.visibility = it
                        employerScales.root.visibility = it
                    }
                    vacancy?.let {
                        employerTxtDetail.text = vacancy.detail
                        employerData.position.text = vacancy.position
                        employerData.dataSalary.text = vacancy.salary
                    } ?: run {
                        employerTxtDetail.text = model.detailTxt
                        employerData.position.text = model.position
                        employerData.dataSalary.text = model.salary
                    }

                    employerData.name.text = model.name
                    employerData.address.text = model.address
                    employerData.titleReviews.visibility = View.GONE
                }
            }
        }
    }

    private fun bindReviewViews(
        binding: FragmentDetailBinding,
        container: ViewGroup?,
        model: BaseModel,
    ) {
        with(binding) {
            for (el in 0 until model.arrayOfComments.size) {
                val viewComment = ReviewLayoutBinding.inflate(layoutInflater, container, false)

                with(model.arrayOfComments[el]) {
                    viewComment.imgAvatar.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            imgAvatarID,
                            null
                        )
                    )
                    viewComment.imgReviewStars.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            imgStarsID,
                            null
                        )
                    )
                    viewComment.reviewName.text = name
                    viewComment.reviewNickname.text = nickname
                    viewComment.txtComment.text = text
                }

                linearLayout.addView(viewComment.root)
            }
        }
    }

    private fun bindInstagramView(binding: FragmentDetailBinding, model: BaseModel) {
        with(binding) {
            instagramData.counterPost.text = model.instagramModel.post.toString()
            instagramData.counterFollower.text = model.instagramModel.follower.toString()
            instagramData.counterFollowing.text = model.instagramModel.following.toString()

            instagramData.btnInstagram.setOnClickListener {
                val uri: Uri = Uri.parse("http://instagram.com/_u/gordongram")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                try {
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            uri
                        )
                    )
                }

            }
        }
    }

    private fun bindLikeBtn(
        binding: FragmentDetailBinding,
        container: ViewGroup?,
        model: BaseModel
    ) {
        with(binding) {
            val btnLikeBinding = BtnLikeBinding.inflate(layoutInflater, container, false)
            btnLikeBinding.btnResponse.setOnClickListener {
                viewModel.swipe()
                prefs.addResponse(model.id, viewModel.list.size)
                findNavController().navigateUp()
            }
            linearLayout.addView(btnLikeBinding.root)
        }
    }

    private fun navigateBack() {
        when (args.previousDestinationId) {
            R.id.navigation_profile_employer -> {
                findNavController().navigate(
                    DetailFragmentDirections.actionToProfileEmployer(1F)
                )
            }
            else -> findNavController().navigateUp()
        }
    }
}