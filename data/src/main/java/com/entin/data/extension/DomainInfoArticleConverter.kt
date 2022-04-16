package com.entin.data.extension

import com.entin.data.model.ArticleFirebaseModel
import com.entin.presentation.model.ArticleDomainModel

/**
 * Converter [ArticleFirebaseModel] to [ArticleDomainModel]
 */
fun ArticleFirebaseModel.convertToArticleDomainModel() =
    ArticleDomainModel(
        title = this.title,
        text = this.text,
        rate = this.rate,
        images = this.images,
        id = this.id,
        date = this.date,
        actionButton = this.actionButton,
    )

/**
 * Converter LIST of [ArticleFirebaseModel] to LIST of [ArticleDomainModel]
 */
fun convertListArticleFirebaseModelToListArticleDomainModel(
    firebaseModelList: List<ArticleFirebaseModel>
): List<ArticleDomainModel> {
    val domainModelList = mutableListOf<ArticleDomainModel>()
    firebaseModelList.forEach {
        domainModelList.add(it.convertToArticleDomainModel())
    }
    return domainModelList.toList()
}