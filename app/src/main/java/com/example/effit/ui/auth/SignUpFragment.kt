package com.example.effit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effit.R
import com.example.effit.data.model.UserModel
import com.example.effit.databinding.FragmentSignUpBinding
import com.example.effit.util.UiState
import com.example.effit.util.hide
import com.example.effit.util.isValidEmail
import com.example.effit.util.show
import com.example.effit.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SignUpFragment : Fragment() {
    val TAG: String = "RegisterFragment"
    lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.signUpButton.setOnClickListener {
            if (validation()){
                viewModel.signUp(
                    email = binding.editTextEmail.text.toString(),
                    password = binding.editTextPassword.text.toString(),
                    user = getUserObj()
                )
            }
        }

        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun observer() {
        viewModel.signUp.observe(viewLifecycleOwner) { state ->
            when(state){
                is UiState.Loading -> {
                    binding.signUpButton.setText("")
                    binding.signUpProgress.show()
                }
                is UiState.Failure -> {
                    binding.signUpButton.setText("Далее")
                    binding.signUpProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.signUpButton.setText("Далее")
                    binding.signUpProgress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_signUpFragment_to_mobile_navigation)
                }
            }
        }
    }

    private fun getUserObj(): UserModel {
        return UserModel(
            id = "",
            name = binding.editTextName.text.toString(),
            password = binding.editTextPassword.text.toString(),
            email = binding.editTextEmail.text.toString(),
        )
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editTextName.text.isNullOrEmpty()){
            isValid = false
            toast(getString(R.string.enter_first_name))
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
}