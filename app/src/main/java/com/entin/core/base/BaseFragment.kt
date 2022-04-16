package com.entin.core.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.entin.core.extension.recursiveLoopViewGroup
import com.entin.core.util.Fail
import com.entin.core.util.Pending
import com.entin.core.util.Success
import com.entin.core.util.ViewResult
import com.entin.presentation.R

/**
 * Base class for all fragments
 */

abstract class BaseFragment : Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Navigation observer
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Best animation transition
        View.TRANSLATION_Z.set(requireView(), 100f)

        viewModel.navEvent.observe(viewLifecycleOwner) { singleEvent ->
            when (val event = singleEvent.getContentIfNotHandled()) {
                is BaseViewModel.NavEvent.To -> navigateTo(event)
                is BaseViewModel.NavEvent.Up -> navigateUp()
                is BaseViewModel.NavEvent.Back -> navigateBack()
                is BaseViewModel.NavEvent.NavigateToRoot -> navigateToRoot()
                else -> {}
            }
        }
    }

    /**
     * Hide all views in the [root] and then call one of the provided lambda functions
     * depending on [viewResult]:
     * - [onPending] is called when [viewResult] is [Pending]
     * - [onSuccess] is called when [viewResult] is [Success]
     * - [onError] is called when [viewResult] is [Fail]
     */
    fun <T> render(
        root: ViewGroup,
        viewResult: ViewResult<T>,
        onPending: () -> Unit,
        onError: () -> Unit,
        onSuccess: (T) -> Unit
    ) {
        root.recursiveLoopViewGroup(false)

        when (viewResult) {
            is Success -> onSuccess(viewResult.data)
            is Fail -> onError()
            is Pending -> onPending()
        }
    }

    /**
     * Logo clicked -> navigate to Main Screen
     */
    fun logoClicked(logoView: View) {
        logoView.setOnClickListener {
            viewModel.logoClicked()
        }
    }

    /**
     * Assign onClick listener for default try again button.
     */
    fun onTryAgain(root: View, onButtonPressed: () -> Unit) =
        root.findViewById<Button>(R.id.error_again_button).setOnClickListener { onButtonPressed() }

    /**
     * Navigation implementation
     */
    private fun navigateTo(event: BaseViewModel.NavEvent.To) =
        findNavController().navigate(
            event.directions.actionId,
            bundleOf(PARAMETER to event.parameter)
        )

    private fun navigateUp() =
        findNavController().navigateUp()

    private fun navigateBack() =
        findNavController().popBackStack()

    private fun navigateToRoot() =
        findNavController().navigate(R.id.mainFragment)
}