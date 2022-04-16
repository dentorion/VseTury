package com.entin.presentation.screens.company

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.entin.core.base.BaseViewModel
import com.entin.core.di.IoDispatcher
import com.entin.presentation.usecase.LogOpenScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val logOpenScreen: LogOpenScreenUseCase,
) : BaseViewModel() {

    override fun tryAgain() {
        TODO("Not yet implemented")
    }

    init {
        viewModelScope.launch(dispatcherIO) {
            logOpenScreen("List tours", "")
        }
    }
}