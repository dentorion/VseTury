package com.entin.data.repository

import com.entin.data.remote.FirebaseRepository
import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : Repository {

    override suspend fun searchNews(): Flow<TourDomainModel> {
        firebaseRepository.searchTours()
        TODO("Not yet implemented")
    }
}