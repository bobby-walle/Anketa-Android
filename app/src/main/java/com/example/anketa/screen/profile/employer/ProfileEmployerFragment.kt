package com.example.anketa.screen.profile.employer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anketa.model.Vacancy
import com.example.anketa.databinding.FragmentProfileEmployerBinding
import com.example.anketa.screen.RvLikeViewPagerAdapter
import com.example.anketa.screen.NavBarCallbacks
import com.example.anketa.screen.tools.OnSnapPositionChangeListener
import com.example.anketa.screen.tools.initLikeViewPager
import com.example.anketa.viewmodel.CardsViewModel

class ProfileEmployerFragment : Fragment() {

    private var _binding: FragmentProfileEmployerBinding? = null
    private val binding get() = _binding!!

    private lateinit var vacancyAdapter: VacancyAdapter
    private var navBarCallbacks: NavBarCallbacks? = null
    private val args: ProfileEmployerFragmentArgs by navArgs()
    val viewModel: CardsViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navBarCallbacks = context as NavBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navBarCallbacks?.showNavBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileEmployerBinding.inflate(inflater, container, false)
        navBarCallbacks?.showNavBar()

        return binding.apply {
            initAdapters(this)
            bindEditProfileClick()
            setMotionProgress(binding, args.motionProgress)
        }.root
    }

    override fun onResume() {
        super.onResume()
        setMotionProgress(binding, args.motionProgress)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindEditProfileClick() {
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(
                ProfileEmployerFragmentDirections.actionProfileEmployerToEditProfileEmployer(false)
            )
        }
    }

    private fun setMotionProgress(binding: FragmentProfileEmployerBinding, motionProgress: Float) {
        with(binding) {
            motionLayout.progress = motionProgress
        }
    }

    private fun initAdapters(binding: FragmentProfileEmployerBinding) {
        with(binding) {
            recyclerProfileEmployer.layoutManager = LinearLayoutManager(context)
            vacancyAdapter = VacancyAdapter(
                { vacancy -> onItemClick(vacancy) },
                { vacancy -> onDetailClicked(vacancy) }
            )
            recyclerProfileEmployer.adapter = vacancyAdapter
            rvLikeViewpager.adapter =
                RvLikeViewPagerAdapter(
                    viewModel.restaurantOneResources,
                    requireContext().packageName
                )

            val layoutManager = LinearLayoutManager(
                context,
                RecyclerView.HORIZONTAL,
                false
            )

            for (el in viewModel.restaurantOneResources) {
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

    private fun onItemClick(vacancy: Vacancy) {
        findNavController().navigate(
            ProfileEmployerFragmentDirections.actionProfileEmployerToVacancy(
                vacancy
            )
        )
    }

    private fun onDetailClicked(vacancy: Vacancy) {
        findNavController().navigate(
            ProfileEmployerFragmentDirections.actionToDetail(
                findNavController().currentDestination?.id!!, true,
                vacancy,
                viewModel.employer1,
            )
        )
    }

    override fun onDetach() {
        super.onDetach()
        navBarCallbacks = null
    }
}