package com.entin.presentation.usecase

import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Get info articles
 */

class GetInfoArticlesUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Result<List<ArticleDomainModel>>> = flow {
        repository.getInfoArticles().collect {
            emit(it)
        }
    }
}