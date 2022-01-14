package com.entin.firebase.repository

import com.entin.firebase.di.FireBaseModule.TOURS
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named

class FirebaseRepositoryImpl @Inject constructor(
    @Named(TOURS) private val fireBaseDb: CollectionReference,
) : FirebaseRepository {


}