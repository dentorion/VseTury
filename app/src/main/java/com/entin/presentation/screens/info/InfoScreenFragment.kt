package com.entin.presentation.screens.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.entin.presentation.R

class InfoScreenFragment : Fragment() {

    private val viewModel by viewModels<InfoViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement the InfoScreenFragment
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

}