package com.entin.presentation.screens.detail

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
import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.screens.list.ListFragmentAction
import com.entin.presentation.usecase.ChangeFavouriteStateUseCase
import com.entin.presentation.usecase.GetFavouriteStateUseCase
import com.entin.presentation.usecase.GetTourByIdUseCase
import com.entin.presentation.usecase.LogOpenScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val getFavouriteStateUseCase: GetFavouriteStateUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val getTourByIdUseCase: GetTourByIdUseCase,
    private val logOpenScreen: LogOpenScreenUseCase,
) : BaseViewModel() {

    private var _viewStateFlow: Flow<ViewResult<DetailViewState>> = combine(
        getActualTour(stateHandle.get<DetailFragmentAction>(PARAMETER)),
        getFavouriteStateOfTour(stateHandle.get<DetailFragmentAction>(PARAMETER)),
        ::mergeStores
    ).flowOn(dispatcherIO)
    private val _viewState: MutableLiveData<ViewResult<DetailViewState>> = MutableLiveData()
    val viewState: LiveData<ViewResult<DetailViewState>> = _viewState

    private var currentTour: TourDomainModel? = null
    private var currentFavouriteState: Boolean? = null

    init {
        viewModelScope.launch(dispatcherIO) {
            _viewState.postValue(Pending())
            _viewStateFlow.collect {
                _viewState.postValue(it)
            }
        }
    }

    /**
     * Download tour from firebase if necessary or show incoming tour
     */
    private fun getActualTour(parameter: DetailFragmentAction?): Flow<TourDomainModel> = flow {
        parameter?.let { parameter ->
            when (parameter) {
                is DetailFragmentAction.Download -> {
                    getTourByIdUseCase(parameter.tourId).collect {
                        it.onSuccess { tour ->
                            emit(tour)
                            currentTour = tour
                        }
                    }
                    /** Logging open screen */
                    logOpenScreen("Detail screen", parameter.tourId.toString())
                }
                is DetailFragmentAction.Full -> {
                    emit(parameter.tour)
                    currentTour = parameter.tour

                    /** Logging open screen */
                    logOpenScreen("Detail screen", parameter.tour.id.toString())
                }
            }
        }
    }.flowOn(dispatcherIO)

    /**
     * Get favourite state of tour
     */
    private fun getFavouriteStateOfTour(parameter: DetailFragmentAction?): Flow<Boolean> =
        flow {
            parameter?.let { parameter ->
                when (parameter) {
                    is DetailFragmentAction.Download -> {
                        getFavouriteStateUseCase(parameter.tourId).collect { state ->
                            emit(state)
                            currentFavouriteState = state
                        }
                    }
                    is DetailFragmentAction.Full -> {
                        getFavouriteStateUseCase(parameter.tour.id).collect { state ->
                            emit(state)
                            currentFavouriteState = state
                        }
                    }
                }
            }
        }.flowOn(dispatcherIO)

    /**
     * Merge 2 flows: dates and cities
     */
    private fun mergeStores(
        tour: TourDomainModel,
        favouriteStatus: Boolean,
    ): ViewResult<DetailViewState> {
        return Success(
            DetailViewState(
                tourToShow = tour,
                favouriteState = favouriteStatus,
            )
        )
    }

    fun changeFavouriteStateOfTour() = viewModelScope.launch(dispatcherIO) {
        currentTour?.let { tour ->
            currentFavouriteState?.let { state ->
                changeFavouriteStateUseCase(tour, state)
            }
        }
    }

    override fun tryAgain() {
        TODO("Not yet implemented")
    }

    fun toursCityFrom(id: Int) = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                DetailScreenFragmentDirections.actionDetailScreenFragmentToListScreenFragment(),
                ListFragmentAction.ToursByCityFrom(id)
            )
        )
    }

    fun toursCityTo(id: Int) = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                DetailScreenFragmentDirections.actionDetailScreenFragmentToListScreenFragment(),
                ListFragmentAction.ToursByCityTo(id)
            )
        )
    }
}