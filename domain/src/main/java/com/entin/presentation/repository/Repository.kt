package com.entin.presentation.repository

import com.entin.presentation.model.TourDomainModel
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun searchNews() : Flow<TourDomainModel>
}