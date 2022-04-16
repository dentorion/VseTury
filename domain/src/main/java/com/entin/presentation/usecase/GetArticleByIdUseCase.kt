package com.entin.presentation.usecase

import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Get Article from firebase by Id
 */

class GetArticleByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(articleId: Int): Flow<Result<ArticleDomainModel>> =
        repository.getArticleById(articleId)
}