package com.example.anketa.screen.profile.employee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anketa.databinding.FragmentEditProfileEmployeeBinding
import com.example.anketa.prefs
import com.example.anketa.screen.NavBarCallbacks

class EditProfileEmployeeFragment : Fragment() {

    private var _binding: FragmentEditProfileEmployeeBinding? = null
    private val binding get() = _binding!!
    private val args: EditProfileEmployeeFragmentArgs by navArgs()
    private var callbacks: NavBarCallbacks? = null
    private lateinit var navigationBack: OnBackPressedCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigationBack()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileEmployeeBinding.inflate(inflater, container, false)

        return binding.apply {
            if (args.isLogin) {
                callbacks?.hideNavBar()
            } else {
                callbacks?.showNavBar()
            }
            initUI(this)
        }.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(binding: FragmentEditProfileEmployeeBinding) {
        with(binding) {
            if (args.isLogin) {
                callbacks?.hideNavBar()

                View.GONE.let {
                    headerStart.visibility = it
                    btnDecline.visibility = it
                    btnLogout.visibility = it
                    btnSave.visibility = it
                }

                View.VISIBLE.let {
                    headerCenterFillProfile.visibility = it
                    btnContinue.visibility = it
                    btnBack.visibility = it
                }

                btnBack.setOnClickListener { findNavController().navigateUp() }

                btnContinue.setOnClickListener {
                    prefs.isProfileDataSet = true
                    findNavController().navigate(
                        EditProfileEmployeeFragmentDirections.actionToHome()
                    )
                }
            } else {
                setBottomMargin(
                    binding,
                    resources.getDimensionPixelSize(androidx.appcompat.R.dimen.abc_action_bar_default_height_material)
                )

                View.GONE.let {
                    btnContinue.visibility = it
                    headerStart.visibility = it
                    btnDecline.visibility = it
                }

                View.VISIBLE.let {
                    headerCenterYourProfile.visibility = it
                    btnLogout.visibility = it
                }

                btnBack.setOnClickListener {
                    requireActivity().onBackPressed()
                }

                btnLogout.setOnClickListener {
                    prefs.clearData()
                    findNavController().navigate(EditProfileEmployeeFragmentDirections.actionToLogin())
                }
            }
        }
    }

    private fun setBottomMargin(binding: FragmentEditProfileEmployeeBinding, margin: Int) {
        val layoutParams = binding.mainLayout.layoutParams as FrameLayout.LayoutParams
        layoutParams.bottomMargin = margin
    }

    private fun initNavigationBack() {
        navigationBack = if (args.isLogin) {
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                findNavController().navigateUp()
            }
        } else {
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                findNavController().navigate(EditProfileEmployeeFragmentDirections.actionToHome())
            }
        }
    }

}