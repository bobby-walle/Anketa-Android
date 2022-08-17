package com.example.anketa.screen.responses

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anketa.R
import com.example.anketa.databinding.FragmentResponsesBinding
import com.example.anketa.model.BaseModel
import com.example.anketa.prefs
import com.example.anketa.screen.NavBarCallbacks
import com.example.anketa.viewmodel.CardsViewModel

class ResponsesFragment : Fragment() {

    private var _binding: FragmentResponsesBinding? = null
    private val binding get() = _binding!!
    private lateinit var responsesAdapter: ResponsesAdapter
    val viewModel: CardsViewModel by activityViewModels()
    private var callbacks: NavBarCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResponsesBinding.inflate(inflater, container, false)
        callbacks?.showNavBar()

        return binding.apply {
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

    private fun initUI(binding: FragmentResponsesBinding) {
        val models = getModels()

        responsesAdapter = ResponsesAdapter(
            models,
            { model -> onItemClick(model) },
            { openTelegram() },
            { openWhatsapp() })

        with(binding) {
            recyclerResponses.layoutManager = LinearLayoutManager(context)
            recyclerResponses.adapter = responsesAdapter
        }
    }

    private fun openTelegram() {
        val uri: Uri = Uri.parse("http://t.me/naqswell")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.instagram.android")

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

    private fun openWhatsapp() {
        val uri: Uri = Uri.parse("https://api.whatsapp.com/send?phone=79312584953")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp.android")

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

    private fun getModels(): ArrayList<BaseModel> =
        arrayListOf<BaseModel>().apply {
            for (id in prefs.getListOfResponsesID()) {
                if (id != null) {
                    for (model in viewModel.list) {
                        if (model.id == id) {
                            add(model)
                        }
                    }
                }
            }
        }

    private fun onItemClick(model: BaseModel) {
        findNavController().navigate(
            ResponsesFragmentDirections.actionToDetail(
                findNavController().currentDestination?.id!!,
                true,
                null,
                model
            )
        )
    }
}