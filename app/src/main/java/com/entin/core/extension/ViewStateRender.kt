package com.entin.core.extension

import android.view.ViewGroup
import androidx.core.view.isVisible
import com.entin.core.base.BaseFragment
import com.entin.core.util.Pending
import com.entin.core.util.Success
import com.entin.core.util.ViewResult
import com.entin.presentation.R
import com.entin.presentation.databinding.PartResultBinding

/**
 * Default [ViewResult] with [viewResult] rendering.
 * - If it is :
 *  - [Pending] -> only progress-bar is displayed
 * -  [Error] -> only error container is displayed
 * -  [Success] -> error container & progress-bar is hidden, all other views are visible
 */
fun <T> BaseFragment.renderResult(
    errorText: String? = null,
    errorTextButton: String? = null,
    root: ViewGroup,
    viewResult: ViewResult<T>,
    onSuccess: (T) -> Unit,
) {
    val binding = PartResultBinding.bind(root)

    render(
        root = root,
        viewResult = viewResult,
        onPending = {
            binding.progressStatus.isVisible = true
        },
        onError = {
            binding.apply {
                listOopsMan.isVisible = true
                errorTextLabel.apply {
                    this.isVisible = true
                    this.text = errorText ?: resources.getString(R.string.list_label_oops_common)
                }
                errorAgainButton.apply {
                    this.isVisible = true
                    this.text = errorTextButton
                        ?: resources.getString(R.string.list_label_oops_button_common)
                }
            }
        },
        onSuccess = { successData ->
            root.recursiveLoopViewGroup(true)
            onSuccess(successData)
        }
    )
}

/**
 * Recursive switching on/off view visibility,
 * regarding to "stop" views from part_result layout
 */
fun ViewGroup.recursiveLoopViewGroup(
    isVisible: Boolean,
) {
    // Common elements for all fragments with part_result layout
    val listOfSupportViews = listOf(
        R.id.progress_status, R.id.list_oops_man, R.id.error_text_label, R.id.error_again_button,
    )

    for (i in this.childCount - 1 downTo 0) {
        val child = this.getChildAt(i)
        if (child is ViewGroup) {
            child.recursiveLoopViewGroup(isVisible)
            // Hide or Show RecyclerView at ListScreen for better user experience
            // on popBack from Detail Screen
            if (child.id == R.id.list_recyclerView) {
                child.isVisible = isVisible
            }
        } else {
            child?.apply {
                if (isVisible) {
                    if (listOfSupportViews.contains(child.id).not()) {
                        child.isVisible = true
                    }
                } else {
                    child.isVisible = false
                }
            }
        }
    }
}