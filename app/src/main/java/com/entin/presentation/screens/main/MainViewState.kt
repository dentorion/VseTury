package com.entin.presentation.screens.main

import com.entin.core.base.ViewState

data class MainViewState(
    val dateFrom: String = "",
    val dateTo: String = "",
    val dateFromCalendar: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val dateToCalendar: Triple<Int, Int, Int> = Triple(0, 0, 0),
    val cityFromPosition: Int = 0,
    val cityToPosition: Int = 0,
) : ViewState