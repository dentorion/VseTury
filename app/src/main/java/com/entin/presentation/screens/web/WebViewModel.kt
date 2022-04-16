package com.entin.presentation.screens.web

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.entin.core.base.BaseViewModel
import com.entin.core.base.PARAMETER
import com.entin.core.di.IoDispatcher
import com.entin.core.util.Pending
import com.entin.core.util.Success
import com.entin.core.util.ViewResult
import com.entin.presentation.usecase.LogOpenScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val logOpenScreen: LogOpenScreenUseCase,
    private val stateHandle: SavedStateHandle,
) : BaseViewModel() {

    private var parameters: String? = null

    private val _webViewState: MutableLiveData<ViewResult<WebViewState>> = MutableLiveData()
    val webViewState: LiveData<ViewResult<WebViewState>> = _webViewState

    init {
        viewModelScope.launch(dispatcherIO) {
            _webViewState.postValue(Pending())

            getSavedStateHandle()
            reactOnSavedStateHandle()

            parameters?.let {
                logOpenScreen("WEB", it)
            }
        }
    }

    private fun getSavedStateHandle() = viewModelScope.launch(dispatcherIO) {
        parameters = stateHandle.get<String>(PARAMETER)
    }

    private fun reactOnSavedStateHandle() = viewModelScope.launch(dispatcherIO) {
        parameters?.let { http ->
            if (http.isNotBlank()) {
                _webViewState.postValue(Pending())
                _webViewState.postValue(Success(WebViewState(http)))
            }
        }
    }

    override fun tryAgain() {
        TODO("Not yet implemented")
    }
}