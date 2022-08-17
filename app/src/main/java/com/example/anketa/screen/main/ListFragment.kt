package com.example.anketa.screen.main

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anketa.databinding.FragmentListBinding
import com.example.anketa.model.BaseModel
import com.example.anketa.prefs
import com.example.anketa.screen.NavBarCallbacks
import com.example.anketa.viewmodel.CardsViewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    val viewModel: CardsViewModel by activityViewModels()
    private var callbacks: NavBarCallbacks? = null

    private lateinit var listAdapter: CardListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavBarCallbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
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

    private fun initUI(binding: FragmentListBinding) {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            listAdapter = CardListAdapter(
                requireContext(),
                viewModel.list,
                { model -> onBtnDetailClick(model) },
                { id -> onBtnLikeClick(id) }
            )
            recyclerView.adapter = listAdapter

            btnSwipes.setOnClickListener {
                findNavController().navigateUp()
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
        }
    }

    private fun onBtnDetailClick(model: BaseModel) {
        findNavController().navigate(
            ListFragmentDirections.actionToDetail(findNavController().currentDestination?.id!!, false, null, model)
        )
    }

    private fun onBtnLikeClick(id: String) {
        prefs.addResponse(id, viewModel.list.size)
        Toast.makeText(requireContext(), "Отклик добавлен", Toast.LENGTH_SHORT).show()
    }
}