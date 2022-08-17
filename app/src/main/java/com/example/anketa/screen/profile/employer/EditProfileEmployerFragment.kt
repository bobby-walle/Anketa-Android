package com.example.anketa.screen.profile.employer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anketa.databinding.FragmentEditProfileEmployerBinding
import com.example.anketa.prefs
import com.example.anketa.screen.NavBarCallbacks

class EditProfileEmployerFragment : Fragment() {

    private var _binding: FragmentEditProfileEmployerBinding? = null
    private val binding get() = _binding!!
    private val args: EditProfileEmployerFragmentArgs by navArgs()
    private var callbacks: NavBarCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
            if (prefs.isProfileDataSet) {
                callbacks?.showNavBar()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileEmployerBinding.inflate(inflater, container, false)

        return binding.apply {
            callbacks?.hideNavBar()
            initUI(this, args.isLogin)
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

    private fun initUI(binding: FragmentEditProfileEmployerBinding, isLogin: Boolean) {
        with(binding) {
            if (isLogin) {
                View.GONE.let {
                    headerStart.visibility = it
                    btnDecline.visibility = it
                    btnInstagram.layout.visibility = it
                    btnLogout.visibility = it
                    btnSave.visibility = it
                }

                View.VISIBLE.let {
                    headerCenterFillProfile.visibility = it
                    btnBack.visibility = it
                    btnContinue.visibility = it
                }

                btnBack.setOnClickListener {
                    findNavController().navigateUp()
                }

                btnContinue.setOnClickListener {
                    callbacks?.showNavBar()
                    prefs.isProfileDataSet = true
                    findNavController().navigate(
                        EditProfileEmployerFragmentDirections.actionToHome()
                    )
                }

            } else {
                View.VISIBLE.let {
                    btnLogout.visibility = it
                    btnSave.visibility = it
                }

                btnContinue.visibility = View.GONE

                btnSave.setOnClickListener {
                    findNavController().navigate(
                        EditProfileEmployerFragmentDirections.actionToEditProfileEmployer(0F)
                    )
                }

                btnDecline.setOnClickListener {
                    findNavController().navigate(
                        EditProfileEmployerFragmentDirections.actionToEditProfileEmployer(0F)
                    )                }

                btnLogout.setOnClickListener {
                    prefs.clearData()
                    findNavController().navigate(EditProfileEmployerFragmentDirections.actionToLogin())
                }
            }
        }
    }
}