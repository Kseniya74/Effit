package com.example.effit.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.effit.R
import com.example.effit.databinding.FragmentNoteListingBinding
import com.example.effit.ui.auth.AuthViewModel
import com.example.effit.util.UiState
import com.example.effit.util.hide
import com.example.effit.util.show
import com.example.effit.util.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class NoteListingFragment : Fragment() {

    val TAG: String = "NoteListingFragment"
    private var param1: String? = null
    lateinit var binding: FragmentNoteListingBinding
    private val viewModel: NoteViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView
    private val adapter by lazy {
        NoteListingAdapter(
            onItemClicked = { pos, item ->
                findNavController().navigate(
                    R.id.action_noteListingFragment_to_noteDetailFragment,
                    Bundle().apply {
                    putParcelable("note", item)
                })
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (this::binding.isInitialized){
            binding.root
        }else {
            binding = FragmentNoteListingBinding.inflate(layoutInflater)
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = staggeredGridLayoutManager
        binding.recyclerView.adapter = adapter
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment)
        }

        authViewModel.getSession {
            viewModel.getNotes(it)
        }

        bottomNavigationView = requireView().findViewById(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Обработка выбранного пункта меню "Home"
                    findNavController().navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_plan -> {
                    // Обработка выбранного пункта меню "Search"
                    findNavController().navigate(R.id.navigation_plan)
                    true
                }

                R.id.navigation_profile -> {
                    // Обработка выбранного пункта меню "Profile"
                    findNavController().navigate(R.id.navigation_profile)
                    true
                }

                R.id.noteListingFragment -> {
                    findNavController().navigate(R.id.noteListingFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun observer(){
        viewModel.note.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.progressBar.show()
                }
                is UiState.Failure -> {
                    binding.progressBar.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.progressBar.hide()
                    adapter.updateList(state.data.toMutableList())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            NoteListingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}