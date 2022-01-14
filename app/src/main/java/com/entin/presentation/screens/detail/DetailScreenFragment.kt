package com.entin.presentation.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.entin.presentation.R

class DetailScreenFragment : Fragment() {

    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement the DetailScreenFragment
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

}