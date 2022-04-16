package com.entin.presentation.screens.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.entin.core.base.BaseViewModel
import com.entin.core.base.PARAMETER
import com.entin.core.di.IoDispatcher
import com.entin.core.util.Pending
import com.entin.core.util.Success
import com.entin.core.util.ViewResult
import com.entin.presentation.model.ArticleButtonAction
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.screens.detail.DetailFragmentAction
import com.entin.presentation.screens.list.ListFragmentAction
import com.entin.presentation.usecase.GetArticleByIdUseCase
import com.entin.presentation.usecase.LogOpenScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    @IoDispatcher private val dispatcherIO: CoroutineDispatcher,
    private val stateHandle: SavedStateHandle,
    private val logOpenScreen: LogOpenScreenUseCase,
    private val getArticleByIdUseCase: GetArticleByIdUseCase,
) : BaseViewModel() {

    private val _articleViewState: MutableLiveData<ViewResult<ArticleViewState>> = MutableLiveData()
    val articleViewState: LiveData<ViewResult<ArticleViewState>> = _articleViewState

    private var parameters: ArticleFragmentAction? = null
    private var currentArticle: ArticleDomainModel? = null

    init {
        viewModelScope.launch(dispatcherIO) {
            logOpenScreen("Info", "")

            getSavedStateHandle()
            reactOnSavedStateHandle()
        }
    }

    private fun getSavedStateHandle() = viewModelScope.launch(dispatcherIO) {
        parameters = stateHandle.get<ArticleFragmentAction>(PARAMETER)
    }

    private fun reactOnSavedStateHandle() = viewModelScope.launch {
        parameters?.let { articleFragmentAction ->
            when (articleFragmentAction) {
                is ArticleFragmentAction.Download -> {
                    _articleViewState.postValue(Pending())
                    getArticleById(articleFragmentAction.articleId)
                }
                is ArticleFragmentAction.Full -> {
                    showArticle(articleFragmentAction.article)
                }
            }
        }
    }

    // Private

    private fun getArticleById(articleId: Int) = viewModelScope.launch(dispatcherIO) {
        getArticleByIdUseCase(articleId).collect { result ->
            result.onSuccess { article ->
                showArticle(article)
            }
        }
    }

    private fun showArticle(article: ArticleDomainModel) = viewModelScope.launch(dispatcherIO) {
        currentArticle = article
        _articleViewState.postValue(Success(ArticleViewState(article)))
    }

    fun actionButtonClicked() = viewModelScope.launch {
        currentArticle?.let { article ->
            when (article.articleButtonAction) {
                is ArticleButtonAction.SearchTourById -> {
                    navigate(
                        NavEvent.To(
                            ArticleScreenFragmentDirections.actionInfoDetailScreenFragmentToDetailScreenFragment(),
                            DetailFragmentAction.Download((article.articleButtonAction as ArticleButtonAction.SearchTourById).parameter)
                        )
                    )
                }
                is ArticleButtonAction.SearchToursByCityFrom -> {
                    navigate(
                        NavEvent.To(
                            ArticleScreenFragmentDirections.actionInfoDetailScreenFragmentToListScreenFragment(),
                            ListFragmentAction.ToursByCityFrom((article.articleButtonAction as ArticleButtonAction.SearchToursByCityFrom).parameter)
                        )
                    )
                }
                is ArticleButtonAction.SearchToursByCityTo -> {
                    navigate(
                        NavEvent.To(
                            ArticleScreenFragmentDirections.actionInfoDetailScreenFragmentToListScreenFragment(),
                            ListFragmentAction.ToursByCityTo((article.articleButtonAction as ArticleButtonAction.SearchToursByCityTo).parameter)
                        )
                    )
                }
                is ArticleButtonAction.WebOpen -> {
                    navigate(
                        NavEvent.To(
                            ArticleScreenFragmentDirections.actionInfoDetailScreenFragmentToWebScreenFragment(),
                            (article.articleButtonAction as ArticleButtonAction.WebOpen).parameter
                        )
                    )
                }
                ArticleButtonAction.Error -> {}
            }
        }
    }

    override fun tryAgain() {
        TODO("Not yet implemented")
    }
}