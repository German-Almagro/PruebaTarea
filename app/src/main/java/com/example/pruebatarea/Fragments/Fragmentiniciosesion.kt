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
import com.example.pruebatarea.databinding.FragmentInicioSesionBinding
import com.example.pruebatarea.viewmodels.LoginUiEvent
import com.example.pruebatarea.viewmodels.LoginViewModel
import kotlinx.coroutines.launch


class Fragmentiniciosesion : Fragment() {

    private lateinit var binding: FragmentInicioSesionBinding

    private val  viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentInicioSesionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dentro de onCreate/onViewCreated:
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Escuchar cambios de estado de la UI
                launch {
                    viewModel.uiState.collect { state ->
                        // Actualizar errores en TextInputLayouts
                        binding.nomIS.error = state.emailError?.let { getString(it) }
                        binding.textInputLayout.error = state.passwordError?.let { getString(it) }

                        // Habilitar / deshabilitar botón
                        binding.button2.isEnabled = state.isLoginEnabled && !state.isLoading
                    }
                }

                // Escuchar eventos de un solo uso
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {

                            is LoginUiEvent.NavigateToHome -> {
                                // Navegar a la siguiente pantalla
                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                                val navController = navHostFragment.navController

                                navController.navigate(R.id.action_fragment_inicio_sesion4_to_homeFragment)
                            }

                            is LoginUiEvent.ShowError -> {
                                Toast.makeText(requireActivity(), getString(event.messageResId), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        binding.button3.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController
            navController.navigate(R.id.action_fragment_inicio_sesion4_to_fragment_recuperarcontra)
            }
        }
        binding.juan.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.nekix.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.button2.setOnClickListener {
            viewModel.onLoginClicked()
        }
    }
}