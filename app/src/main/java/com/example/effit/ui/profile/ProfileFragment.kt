package com.example.effit.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effit.R
import com.example.effit.data.model.UserModel
import com.example.effit.databinding.FragmentProfileBinding
import com.example.effit.util.UiState
import com.example.effit.util.hide
import com.example.effit.util.isValidEmail
import com.example.effit.util.show
import com.example.effit.util.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private val profileViewModel: ProfileViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideInputs()
        observer()
        binding.editProfile.setOnClickListener {
            showInputs()
            binding.saveButton.setOnClickListener {
                if (validation()){
                    profileViewModel.editUser(
                        email = binding.editTextEmail.text.toString(),
                        password = binding.editTextPassword.text.toString(),
                        name = binding.editTextName.text.toString(),
                        user = getUserObj()
                    )
                    hideInputs()
                }
            }
            binding.cancel.setOnClickListener {
                hideInputs()
            }
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

    private fun observer() {
        profileViewModel.updateUser.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {

                }
                is UiState.Failure -> {
                    binding.saveButton.hide()
                    binding.cancel.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.saveButton.hide()
                    binding.cancel.hide()
                    toast(state.data)
                }
            }
        }
    }

    private fun getUserObj(): UserModel {
        return UserModel(
            id = "",
            name = binding.editTextName.text.toString(),
            email = binding.editTextEmail.text.toString(),
            password = binding.editTextPassword.text.toString(),
        )
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editTextName.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_name))
        }

        if (binding.editTextEmail.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_email))
        }else{
            if (!binding.editTextEmail.text.toString().isValidEmail()){
                isValid = false
                toast(getString(R.string.invalid_email))
            }
        }
        if (binding.editTextPassword.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_password))
        }else{
            if (binding.editTextPassword.text.toString().length < 8){
                isValid = false
                toast(getString(R.string.invalid_password))
            }
        }
        return isValid
    }

    private fun showInputs() {
        binding.backgroundProfile.hide()
        binding.editTextEmail.isEnabled = true
        binding.editTextEmail.layoutParams.height = 150
        binding.editTextEmail.layoutParams.width = 900
        binding.editTextName.isEnabled = true
        binding.editTextName.layoutParams.height = 150
        binding.editTextName.layoutParams.width = 900
        binding.editTextPassword.isEnabled = true
        binding.editTextPassword.layoutParams.height = 150
        binding.editTextPassword.layoutParams.width = 900
        binding.saveButton.show()
        binding.cancel.show()
    }

    private fun hideInputs() {
        binding.backgroundProfile.show()
        binding.editTextEmail.isEnabled = false
        binding.editTextEmail.layoutParams.height = 150
        binding.editTextEmail.layoutParams.width = 900
        binding.editTextName.isEnabled = false
        binding.editTextName.layoutParams.height = 150
        binding.editTextName.layoutParams.width = 900
        binding.editTextPassword.isEnabled = false
        binding.editTextPassword.layoutParams.height = 150
        binding.editTextPassword.layoutParams.width = 900
        binding.saveButton.hide()
        binding.cancel.hide()
    }

    override fun onStart() {
        super.onStart()
        profileViewModel.showProfile { user ->
            binding.editTextName.setText(user.name)
            binding.editTextEmail.setText(user.email)
        }
    }
}