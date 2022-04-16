package com.entin.presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.entin.core.base.BaseViewModel
import com.entin.core.di.IoDispatcher
import com.entin.core.util.Success
import com.entin.core.util.TravelCitiesStore
import com.entin.core.util.TravelDatesStore
import com.entin.core.util.ViewResult
import com.entin.presentation.model.SliderAction
import com.entin.presentation.model.SliderItemModel
import com.entin.presentation.screens.article.ArticleFragmentAction
import com.entin.presentation.screens.list.ListFragmentAction
import com.entin.presentation.usecase.GetSliderItemsUseCase
import com.entin.presentation.usecase.LogOpenScreenUseCase
import com.entin.presentation.usecase.LogSearchQueryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * MainViewModel for [MainScreenFragment]
 *
 * Prepare arguments for searching tours.
 * Queue of dates on timeline should be:
 * --Q1-T1--------T2-Q2--, where
 * Q1 is query datetime of tour beginning by user choice 00 AM
 * T1 is datetime of tour beginning in firebase collection 12 PM
 * T2 is datetime of tour ending in firebase collection )) 12 PM
 * Q2 is query datetime of tour ending by user choice 23 PM
 *
 * DateTo (Calendar) (Q2) of user choice is set to 23 o'clock
 * to be sure it goes after firebase tour dateTo time (12:00)
 */

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val travelDatesStore: TravelDatesStore,
    private val travelCitiesStore: TravelCitiesStore,
    private val logSearchQueryUseCase: LogSearchQueryUseCase,
    private val getSliderItemsUseCase: GetSliderItemsUseCase,
    private val logOpenScreen: LogOpenScreenUseCase,
) : BaseViewModel() {

    private var positionSlider = 0

    val viewState: LiveData<ViewResult<MainViewState>> = combine(
        travelDatesStore.travelDates,
        travelCitiesStore.travelCities,
        ::mergeStores
    ).flowOn(dispatcherIO).distinctUntilChanged { old, new ->
        if (new is Success && old is Success) old.data == new.data
        else false
    }.asLiveData()

    /** Slider observer */
    private val _sliderItems = MutableLiveData<List<SliderItemModel>>(listOf())
    val sliderItems: LiveData<List<SliderItemModel>> = _sliderItems

    /**
     * Request slider content with actions
     */
    init {
        viewModelScope.launch(dispatcherIO) {
            getSliderItemsUseCase().collect { result ->
                result.onSuccess {
                    _sliderItems.postValue(result.getOrThrow())
                }
            }

            logOpenScreen("Main page", "")
        }
    }

    /**
     * Search tours: send logs & navigate to ListScreenFragment
     */
    fun searchTours(cityFromId: Int, cityToId: Int) = viewModelScope.launch(dispatcherIO) {
        val cities = Pair(cityFromId, cityToId)
        val dates = Pair(
            travelDatesStore.travelDates.first().first,
            travelDatesStore.travelDates.first().second
        )

        logSearchQueryUseCase(
            cities = cities,
            dates = dates
        )

        navigate(
            NavEvent.To(
                MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                ListFragmentAction.Search(Pair(cities, dates))
            )
        )
    }

    /**
     * Slider item clicked
     */
    fun sliderItemClicked(sliderItemModel: SliderItemModel) = viewModelScope.launch(dispatcherIO) {
        when (val action = sliderItemModel.sliderAction) {
            is SliderAction.ArticlePage -> {
                navigate(
                    NavEvent.To(
                        MainScreenFragmentDirections.actionMainFragmentToArticleScreenFragment(),
                        ArticleFragmentAction.Download(action.parameter)
                    )
                )
            }
            is SliderAction.SearchToursByCityTo -> {
                navigate(
                    NavEvent.To(
                        MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                        ListFragmentAction.ToursByCityTo(action.parameter)
                    )
                )
            }
            is SliderAction.SearchToursByCityFrom -> {
                navigate(
                    NavEvent.To(
                        MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                        ListFragmentAction.ToursByCityFrom(action.parameter)
                    )
                )
            }
            is SliderAction.WebOpen -> {
                navigate(
                    NavEvent.To(
                        MainScreenFragmentDirections.actionMainFragmentToWebScreenFragment(),
                        action.parameter
                    )
                )
            }
            SliderAction.Error -> {}
        }
    }

    /**
     * Slider position logic
     */

    fun positionSliderZero() {
        positionSlider = 0
    }

    fun positionSliderPlus() {
        positionSlider += 1
    }

    fun positionSliderGet(): Int =
        positionSlider

    fun positionSliderSet(value: Int) {
        positionSlider = value
    }

    /**
     * Navigation
     */

    fun navigateToFavouriteTours() = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                ListFragmentAction.FavouriteTours
            )
        )
    }

    fun navigateToUpcomingTours() = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                ListFragmentAction.Upcoming
            )
        )
    }

    fun navigateToInfoArticlesList() = viewModelScope.launch(dispatcherIO) {
        navigate(
            NavEvent.To(
                MainScreenFragmentDirections.actionMainFragmentToListScreenFragment(),
                ListFragmentAction.Articles
            )
        )
    }

    /**
     *
     */
    override fun tryAgain() {
        TODO("Not yet implemented")
    }

    /**
     * Set position city from
     */
    fun setCityFromPosition(value: Int) = viewModelScope.launch(dispatcherIO) {
        travelCitiesStore.setCityFromPosition(value)
    }

    /**
     * Set position city to
     */
    fun setCityToPosition(value: Int) = viewModelScope.launch(dispatcherIO) {
        travelCitiesStore.setCityToPosition(value)
    }

    /**
     * User choose dateFrom
     */
    fun setDateFrom(year: Int, month: Int, day: Int) = viewModelScope.launch(dispatcherIO) {
        Calendar.getInstance().also {
            it.set(year, month, day, 0, 0, 0)
            travelDatesStore.setDateFrom(it.timeInMillis)
        }
    }

    /**
     * User choose dateTo
     */
    fun setDateTo(year: Int, month: Int, day: Int) = viewModelScope.launch(dispatcherIO) {
        Calendar.getInstance().also {
            it.set(year, month, day, HOURS_23, 0, 0)
            travelDatesStore.setDateTo(it.timeInMillis)
        }
    }

    /**
     * Merge 2 flows: dates and cities
     */
    private fun mergeStores(
        dates: Pair<Long, Long>,
        cities: Pair<Int, Int>,
    ): ViewResult<MainViewState> {
        return Success(
            MainViewState(
                dateFrom = convertDateLongToString(dates.first),
                dateTo = convertDateLongToString(dates.second),
                dateFromCalendar = convertDateLongToYearMonthDay(dates.first),
                dateToCalendar = convertDateLongToYearMonthDay(dates.second),
                cityFromPosition = cities.first,
                cityToPosition = cities.second,
            )
        )
    }

    /**
     * Convert Unix Time for calendar
     */
    private fun convertDateLongToYearMonthDay(value: Long): Triple<Int, Int, Int> {
        val timeD = Date(value)
        val year = SimpleDateFormat("Y", Locale.US).format(timeD).toInt()
        val month = SimpleDateFormat("M", Locale.US).format(timeD).toInt()
        val day = SimpleDateFormat("d", Locale.US).format(timeD).toInt()
        return Triple(year, month, day)
    }

    /**
     * Convert Unix Time for buttons
     */
    private fun convertDateLongToString(value: Long) =
        SimpleDateFormat("d LLLL", Locale.getDefault()).format(Date(value))

    companion object {
        private const val HOURS_23 = 23
    }
}