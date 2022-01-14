package com.entin.presentation.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.entin.presentation.R

class ListScreenFragment : Fragment() {

    private val viewModel by viewModels<ListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: Implement the ListScreenFragment
        return inflater.inflate(R.layout.list_fragment, container, false)
    }

}