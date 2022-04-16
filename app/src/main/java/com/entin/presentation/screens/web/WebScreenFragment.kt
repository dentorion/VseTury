package com.entin.presentation.screens.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.entin.core.base.BaseFragment
import com.entin.core.extension.renderResult
import com.entin.presentation.databinding.WebFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WebScreenFragment : BaseFragment() {

    private var _binding: WebFragmentBinding? = null
    private val binding get() = _binding!!
    override val viewModel by viewModels<WebViewModel>()
    private var webViewClient: WebViewClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WebFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Create WebView */
        initWebView()
        /** Get parameters of fragment */
        observeViewState()
    }

    private fun initWebView() {
        webViewClient = WebViewClient()
    }

    /**
     * Get arguments after navigation to this fragment
     */
    private fun observeViewState() {
        viewModel.webViewState.observe(viewLifecycleOwner) { viewResult ->
            renderResult(root = binding.root, viewResult = viewResult) { viewState ->
                /** Set views */
                openWebPage(viewState.http)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openWebPage(http: String) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.webView.apply {
                    settings.javaScriptEnabled = true
                    settings.setSupportZoom(true)
                    loadUrl(http)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webViewClient = null
        _binding = null
    }
}