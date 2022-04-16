package com.entin.core.util

import android.view.View
import androidx.annotation.StringRes
import com.entin.presentation.R
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar


/**
 * Build the Snackbar without show()
 *
 * @param messageRes Text to be shown inside the Snackbar
 * @param length Duration of the Snackbar
 */
fun View.showSnackbar(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_SHORT): Snackbar =
    Snackbar.make(this, resources.getString(messageRes), length).apply {
        this.setBackgroundTint(
            this.context.resources.getColor(
                R.color.lemon,
                this.context.theme
            )
        )
        this.setTextColor(
            this.context.resources.getColor(
                R.color.real_black, this.context.theme
            )
        )
        this.animationMode = ANIMATION_MODE_SLIDE
    }


