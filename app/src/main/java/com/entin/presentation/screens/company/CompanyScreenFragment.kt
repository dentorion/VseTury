package com.entin.presentation.screens.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.entin.core.base.BaseFragment
import com.entin.presentation.databinding.CompanyFragmentBinding
import com.entin.presentation.model.TourDomainModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompanyScreenFragment : BaseFragment() {

    override val viewModel by viewModels<CompanyViewModel>()
    private var _binding: CompanyFragmentBinding? = null
    private val binding get() = _binding!!
    private var parameters: TourDomainModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CompanyFragmentBinding.inflate(inflater, container, false)

        /** Get parameters of fragment */
        getFragmentArguments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun getFragmentArguments() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        parameters = null
        _binding = null
    }
}