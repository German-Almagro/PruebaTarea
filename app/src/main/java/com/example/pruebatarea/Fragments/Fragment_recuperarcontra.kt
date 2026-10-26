package com.example.pruebatarea.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.pruebatarea.R
import com.example.pruebatarea.databinding.FragmentInicioSesionBinding
import com.example.pruebatarea.databinding.FragmentRecuperarcontraBinding


class Fragment_recuperarcontra : Fragment() {

    private lateinit var binding: FragmentRecuperarcontraBinding

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
    }


}