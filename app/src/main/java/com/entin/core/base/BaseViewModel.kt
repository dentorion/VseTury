package com.entin.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.entin.core.util.SingleLiveEvent
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    /**
     * Try again button reaction in a case of ViewError state
     */
    abstract fun tryAgain()

    /**
     * Navigation
     */
    sealed class NavEvent {
        data class To(val directions: NavDirections, val parameter: Any?) : NavEvent()
        object Up : NavEvent()
        object Back : NavEvent()
        object NavigateToRoot : NavEvent()
    }

    private val _mutableNavEvent: MutableLiveData<SingleLiveEvent<NavEvent>> = MutableLiveData()
    val navEvent: LiveData<SingleLiveEvent<NavEvent>> = _mutableNavEvent

    fun navigate(navEvent: NavEvent) = viewModelScope.launch {
        _mutableNavEvent.value = SingleLiveEvent(navEvent)
    }

    /**
     * Logo clicked -> navigate to Main Screen
     */
    fun logoClicked() {
        navigate(NavEvent.NavigateToRoot)
    }
}