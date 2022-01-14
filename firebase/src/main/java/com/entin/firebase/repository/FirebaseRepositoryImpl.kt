package com.entin.firebase.repository

import com.entin.data.remote.FirebaseRepository
import com.entin.firebase.di.FireBaseModule.TOURS
import com.entin.data.dto.TourFirebaseModel
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class FirebaseRepositoryImpl @Inject constructor(
    @Named(TOURS) private val fireBaseDb: CollectionReference,
) : FirebaseRepository {

    override suspend fun searchTours(): Result<List<TourFirebaseModel>> {
        fireBaseDb.document("RUHeRje5OJJf9HcN4WEQ").get().await()
        TODO("Not yet implemented")
    }
}