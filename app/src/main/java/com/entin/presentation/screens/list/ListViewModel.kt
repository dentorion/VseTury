package com.entin.presentation.screens.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.entin.core.base.BaseViewModel
import com.entin.core.base.PARAMETER
import com.entin.core.di.IoDispatcher
import com.entin.core.util.Fail
import com.entin.core.util.Pending
import com.entin.core.util.Success
import com.entin.core.util.ViewResult
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.screens.article.ArticleFragmentAction
import com.entin.presentation.screens.detail.DetailFragmentAction
import com.entin.presentation.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val stateHandle: SavedStateHandle,
    private val searchToursUseCase: SearchToursUseCase,
    private val getFavouriteToursUseCase: FavouriteToursUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val getUpcomingToursUseCase: GetUpcomingToursUseCase,
    private val getToursCityFromUseCase: GetToursCityFromUseCase,
    private val getToursCityToUseCase: GetToursCityToUseCase,
    private val logOpenScreen: LogOpenScreenUseCase,
    private val getInfoArticlesUseCase: GetInfoArticlesUseCase,
) : BaseViewModel() {

    private val _listViewState: MutableLiveData<ViewResult<ListViewState>> = MutableLiveData()
    val listViewState: LiveData<ViewResult<ListViewState>> = _listViewState

    companion object {
        private const val NAME_SCREEN = "List screen"
        private const val SEARCH_SCREEN = "common search"
        private const val TO_SCREEN = "search to city"
        private const val FROM_SCREEN = "search from city"
        private const val FAVOURITE_SCREEN = "favourite list"
        private const val UPCOMING_SCREEN = "upcoming tours"
    }

    init {
        viewModelScope.launch {
            _listViewState.postValue(Pending())

            when(val parameters: ListFragmentAction? = stateHandle.get<ListFragmentAction>(PARAMETER)) {
                ListFragmentAction.FavouriteTours -> {
                    favouriteTours()
                }
                ListFragmentAction.Articles -> {
                    getArticles()
                }
                is ListFragmentAction.Search -> {
                    searchTours(
                        cityFromId = parameters.data.first.first,
                        cityToId = parameters.data.first.second,
                        dateFrom = parameters.data.second.first,
                        dateTo = parameters.data.second.second,
                    )
                }
                is ListFragmentAction.ToursByCityFrom -> {
                    searchToursFromCity(parameters.data)
                }
                is ListFragmentAction.ToursByCityTo -> {
                    searchToursToCity(parameters.data)
                }
                ListFragmentAction.Upcoming -> {
                    upcomingTours()
                }
                null -> {}
            }
        }
    }

    private fun searchTours(
        cityFromId: Int,
        cityToId: Int,
        dateFrom: Long,
        dateTo: Long,
    ) = viewModelScope.launch(dispatcherIO) {
        searchToursUseCase(cityFromId, cityToId, dateFrom, dateTo).collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState((list))))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }

        /** Logging open screen */
        logOpenScreen(NAME_SCREEN, SEARCH_SCREEN)
    }

    /**
     * Favourite tours loading
     */
    private fun favouriteTours() = viewModelScope.launch(dispatcherIO) {
        _listViewState.postValue(Pending())

        getFavouriteToursUseCase().collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState((list))))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }

        /** Logging open screen */
        logOpenScreen(NAME_SCREEN, FAVOURITE_SCREEN)
    }

    /**
     * Navigate to Detail screen with parameter [TourDomainModel]
     */
    fun navigateToDetailScreenTour(tourDomainModel: TourDomainModel) =
        viewModelScope.launch(dispatcherIO) {
            navigate(
                NavEvent.To(
                    ListScreenFragmentDirections.actionListScreenFragmentToDetailScreenFragment(),
                    DetailFragmentAction.Full(tour = tourDomainModel)
                )
            )
        }

    /**
     * Navigate to DetailTour screen with downloading actual info about tour
     */
    fun navigateToDetailScreenTour(tourId: Int) = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                ListScreenFragmentDirections.actionListScreenFragmentToDetailScreenFragment(),
                DetailFragmentAction.Download(tourId = tourId)
            )
        )
    }

    /**
     * Navigate to DetailArticle screen
     */
    fun navigateToDetailScreenInfo(infoArticle: ArticleDomainModel) =
        viewModelScope.launch(dispatcherIO) {
            navigate(
                NavEvent.To(
                    ListScreenFragmentDirections.actionListScreenFragmentToInfoDetailScreenFragment(),
                    ArticleFragmentAction.Full(infoArticle)
                )
            )
        }

    /**
     * OnSwipe delete tour from favourite list
     */
    fun changeFavouriteStatusOfTour(tour: TourDomainModel) = viewModelScope.launch {
        changeFavouriteStateUseCase.invoke(tour = tour, state = true)
    }

    /**
     * Soon tours
     */
    private fun upcomingTours() = viewModelScope.launch(dispatcherIO) {
        _listViewState.postValue(Pending())

        getUpcomingToursUseCase().collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState((list))))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }

        /** Logging open screen */
        logOpenScreen(NAME_SCREEN, UPCOMING_SCREEN)
    }

    /**
     * Search tours to city by id
     */
    private fun searchToursToCity(cityId: Int) = viewModelScope.launch(dispatcherIO) {
        _listViewState.postValue(Pending())

        getToursCityToUseCase(cityId).collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState((list))))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }

        /** Logging open screen */
        logOpenScreen(NAME_SCREEN, TO_SCREEN)
    }

    /**
     * Search tours from city by id
     */
    private fun searchToursFromCity(cityId: Int) = viewModelScope.launch(dispatcherIO) {
        _listViewState.postValue(Pending())

        getToursCityFromUseCase(cityId).collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState(tours = list)))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }

        /** Logging open screen */
        logOpenScreen(NAME_SCREEN, FROM_SCREEN)
    }

    /**
     * Get all articles
     */
    private fun getArticles() = viewModelScope.launch(dispatcherIO) {
        _listViewState.postValue(Pending())

        getInfoArticlesUseCase().collect { result ->
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    _listViewState.postValue(Success(ListViewState(articles = list)))
                } else {
                    _listViewState.postValue(Fail())
                }
            }
        }
    }

    /**
     * Error button onClick reaction
     */
    override fun tryAgain() =
        logoClicked()

}