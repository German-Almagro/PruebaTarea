package com.example.pruebatarea.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.example.pruebatarea.R
import com.example.pruebatarea.databinding.FragmentRecuperarcontraBinding
import com.example.pruebatarea.viewmodels.LoginUiEvent
import com.example.pruebatarea.viewmodels.RecoverUiEvent
import com.example.pruebatarea.viewmodels.RecoverViewModel
import kotlinx.coroutines.launch


class Fragmentrecuperarcontra : Fragment() {

    private lateinit var binding: FragmentRecuperarcontraBinding


    private val viewModel: RecoverViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecuperarcontraBinding.inflate(inflater)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button4.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController
            navController.navigate(R.id.action_fragment_recuperarcontra_to_fragment_inicio_sesion4)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Escuchar cambios de estado de la UI
                launch {
                    viewModel.uiState.collect { state ->
                        // Actualizar errores en TextInputLayouts
                        binding.textInputLayout3.error = state.emailError?.let { getString(it) }
                        binding.textInputLayout4.error = state.passwordError?.let { getString(it) }

                        // Habilitar / deshabilitar botón
                        binding.button4.isEnabled = state.isLoginEnabled && !state.isLoading
                    }
                }

                // Escuchar eventos de un solo uso
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {

                            is RecoverUiEvent.NavigateToHome -> {
                                // Navegar a la siguiente pantalla
                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                                val navController = navHostFragment.navController

                                navController.navigate(R.id.action_fragment_recuperarcontra_to_fragment_inicio_sesion4)
                            }

                            is RecoverUiEvent.ShowError -> {
                                Toast.makeText(requireActivity(), getString(event.messageResId), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        binding.corr.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.contra.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.button4.setOnClickListener {
            viewModel.onLoginClicked()
        }


    }


}