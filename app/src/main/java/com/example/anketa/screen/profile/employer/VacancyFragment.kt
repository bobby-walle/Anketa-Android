package com.example.anketa.screen.profile.employer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anketa.R
import com.example.anketa.model.Vacancy
import com.example.anketa.databinding.FragmentVacancyBinding
import com.example.anketa.screen.NavBarCallbacks

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!

    private val args: VacancyFragmentArgs by navArgs()
    private var callbacks: NavBarCallbacks? = null
    private var navigateToProfile: OnBackPressedCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToProfile =
            requireActivity().onBackPressedDispatcher.addCallback(this) {
                findNavController().navigate(VacancyFragmentDirections.actionToProfile(1F))
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        callbacks?.hideNavBar()

        return binding.apply {
            initUI(this)
        }.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun initUI(binding: FragmentVacancyBinding) {
        setUserData(binding, args.vacancy)

        with(binding) {
            btnShare.setOnClickListener {
                findNavController().navigate(VacancyFragmentDirections.actionToProfile(1F))
            }
            btnDecline.setOnClickListener {
                findNavController().navigate(VacancyFragmentDirections.actionToProfile(1F))
            }
        }
    }

    private fun setUserData(binding: FragmentVacancyBinding, vacancy: Vacancy) {
        with(binding) {
            vacancyPosition.editTxt.setText(vacancy.position)
            vacancyPosition.editTxtTitle.text = getString(R.string.vacancy_edit_txt_title)

            vacancySalary.editTxt.setText(vacancy.salary)
            vacancySalary.editTxtTitle.text = getString(R.string.salary_edit_txt_title)

            vacancyExperience.editTxt.setText(vacancy.experience)
            vacancyExperience.editTxtTitle.text = getString(R.string.experience_edit_txt_title)

            vacancyAbout.editTxt.setText(vacancy.detail)
            vacancyAbout.editTxtTitle.text = getString(R.string.detail_edit_txt_title)
        }
    }
}