package com.entin.data.remote

import com.entin.data.dto.TourFirebaseModel

interface FirebaseRepository {

    suspend fun searchTours(): Result<List<TourFirebaseModel>>
}