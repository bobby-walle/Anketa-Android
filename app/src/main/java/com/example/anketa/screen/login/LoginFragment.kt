package com.example.anketa.screen.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.anketa.data.Role
import com.example.anketa.databinding.FragmentLoginBinding
import com.example.anketa.prefs
import com.example.anketa.screen.NavBarCallbacks

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var callbacks: NavBarCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
//            to disable system back button
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.apply {
            callbacks?.hideNavBar()
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

    private fun initUI(binding: FragmentLoginBinding) {
        with(binding) {
            val actionToEmployer = LoginFragmentDirections.actionLoginToEditProfileEmployer(true)
            btnEmployer.setOnClickListener {
                prefs.role = Role.Employer
                findNavController().navigate(actionToEmployer)
            }

            val actionToEmployee =
                LoginFragmentDirections.actionLoginToEditProfileEmployee(true)
            btnEmployee.setOnClickListener {
                prefs.role = Role.Employee
                findNavController().navigate(actionToEmployee)
            }
        }
    }
}