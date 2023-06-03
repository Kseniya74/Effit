package com.example.effit.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.effit.R
import com.example.effit.databinding.FragmentSignInBinding
import com.example.effit.util.UiState
import com.example.effit.util.hide
import com.example.effit.util.isValidEmail
import com.example.effit.util.show
import com.example.effit.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.button.setOnClickListener {
            if (validation()) {
                viewModel.signIn(
                    email = binding.editTextEmail.text.toString(),
                    password = binding.editTextPassword.text.toString()
                )
            }
        }

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.restorePassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }
    }

    private fun observer() {
        viewModel.signIn.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.button.setText("")
                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
                    binding.button.setText("Войти")
                    binding.loginProgress.hide()
                    toast(state.error)
                }
                is UiState.Success<*> -> {
                    binding.button.setText("Войти")
                    binding.loginProgress.hide()
                    toast(state.data as String?)
                    findNavController().navigate(R.id.action_signInFragment_to_mobile_navigation)
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.editTextEmail.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_email))
        }
        else {
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

    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null)
                findNavController().navigate(R.id.action_signInFragment_to_mobile_navigation)
        }
    }
}