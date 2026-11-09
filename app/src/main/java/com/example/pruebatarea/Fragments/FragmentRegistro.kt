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
import com.example.pruebatarea.databinding.FragmentRegistroBinding
import com.example.pruebatarea.viewmodels.RecoverUiEvent
import com.example.pruebatarea.viewmodels.RegistroUiEvent
import com.example.pruebatarea.viewmodels.RegistroViewModel
import kotlinx.coroutines.launch


class FragmentRegistro : Fragment() {

    private lateinit var binding: FragmentRegistroBinding

    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {

            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController

            navController.navigate(R.id.action_fragmentRegistro_to_fragment_inicio_sesion4)

        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Escuchar cambios de estado de la UI
                launch {
                    viewModel.uiState.collect { state ->
                        // Actualizar errores en TextInputLayouts
                        binding.TINOMBRE.error = state.emailError?.let { getString(it) }
                        binding.TINCONTRA.error = state.passwordError?.let { getString(it) }
                        binding.textInputLayout2.error = state.confirmPasswordError?.let { getString(it) }

                        // Habilitar / deshabilitar botón
                        binding.button.isEnabled = state.isRegistroEnabled && !state.isLoading
                    }
                }

                // Escuchar eventos de un solo uso
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {

                            is RegistroUiEvent.NavigateToHome -> {
                                // Navegar a la siguiente pantalla
                                val navHostFragment = requireActivity().supportFragmentManager
                                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

                                val navController = navHostFragment.navController

                                navController.navigate(R.id.action_fragmentRegistro_to_fragment_inicio_sesion4)
                            }

                            is RegistroUiEvent.ShowError -> {
                                Toast.makeText(requireActivity(), getString(event.messageResId), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        binding.correo.doOnTextChanged { text, _, _, _ ->
            viewModel.onEmailChanged(text.toString())
        }

        binding.contrase.doOnTextChanged { text, _, _, _ ->
            viewModel.onPasswordChanged(text.toString())
        }

        binding.confirmarcontra.doOnTextChanged { text, _, _,_->
            viewModel.onConfirmPasswordChanged(text.toString())
        }

        binding.button.setOnClickListener {
            viewModel.onRegistroClicked()
        }




    }
}