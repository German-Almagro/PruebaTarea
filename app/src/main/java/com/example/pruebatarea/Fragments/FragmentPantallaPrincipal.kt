package com.example.pruebatarea.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.pruebatarea.R
import com.example.pruebatarea.databinding.FragmentPantallaPrincipalBinding


class FragmentPantallaPrincipal : Fragment() {
    private lateinit var binding: FragmentPantallaPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPantallaPrincipalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.InSe.setOnClickListener {

            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController

            navController.navigate(R.id.action_fragmentPantallaPrincipal2_to_fragment_inicio_sesion4)

        }

        binding.Reg.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController

            navController.navigate(R.id.action_fragmentPantallaPrincipal2_to_fragmentRegistro) }

        binding.LinkError.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

            val navController = navHostFragment.navController

            navController.navigate(R.id.action_fragmentPantallaPrincipal2_to_fragmentPantallaError) }

        }

    }
