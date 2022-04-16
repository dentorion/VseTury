package com.entin.presentation.screens.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.entin.core.base.BaseFragment
import com.entin.core.extension.renderResult
import com.entin.core.util.scrollLeft
import com.entin.core.util.scrollRight
import com.entin.presentation.R
import com.entin.presentation.databinding.ArticleFragmentBinding
import com.entin.presentation.model.ArticleButtonAction
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.screens.list.adapter.slider.ArticleImageSliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ArticleScreenFragment : BaseFragment() {

    private var _binding: ArticleFragmentBinding? = null
    private val binding get() = _binding!!
    override val viewModel by viewModels<ArticleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArticleFragmentBinding.inflate(inflater, container, false)

        /** Get parameters of fragment */
        observeViewState()
        /** Set onClickListeners */
        setupOnClickListeners()

        return binding.root
    }

    /**
     * Get arguments after navigation to this fragment
     */
    private fun observeViewState() {
        viewModel.articleViewState.observe(viewLifecycleOwner) { viewResult ->
            renderResult(root = binding.root, viewResult = viewResult) { viewState ->
                /** Set views */
                setData(viewState.article)
            }
        }
    }

    /**
     * Set slider, title, text
     */
    private fun setData(article: ArticleDomainModel) {
        binding.apply {
            /** Text */
            infoFragmentTitle.text = article.title
            infoFragmentText.text = article.text
            /** Slider */
            infoFragmentSlider.apply {
                adapter = ArticleImageSliderAdapter(
                    list = article.images,
                    isRoundedCorner = false
                )
                clipToPadding = false
                clipChildren = false
                getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

                // Animation of scrolling
                setPageTransformer(CompositePageTransformer().apply {
                    addTransformer(MarginPageTransformer(40))
                    addTransformer { page, position ->
                        val r: Float = 1 - abs(position)
                        page.scaleY = 0.85f + r * 0.15f
                    }
                })
            }
            /** Action Button */
            button.apply {
                text = when (article.articleButtonAction) {
                    ArticleButtonAction.Error ->
                        resources.getString(R.string.detail_action_button_title_error)
                    is ArticleButtonAction.SearchTourById ->
                        resources.getString(R.string.detail_action_button_title_search_tour_by_id)
                    is ArticleButtonAction.SearchToursByCityFrom ->
                        resources.getString(R.string.detail_action_button_title_search_tour_by_city_from)
                    is ArticleButtonAction.SearchToursByCityTo ->
                        resources.getString(R.string.detail_action_button_title_tour_by_city_to)
                    is ArticleButtonAction.WebOpen ->
                        resources.getString(R.string.detail_action_button_title_web_open)
                }
            }
            /** Arrows for slider */
            itemInfoSliderArrowRight.setOnClickListener {
                infoFragmentSlider.scrollRight()
            }

            itemInfoSliderArrowLeft.setOnClickListener {
                infoFragmentSlider.scrollLeft()
            }
        }
    }

    /**
     * Setup onClickListeners
     */
    private fun setupOnClickListeners() {
        binding.apply {
            /** Logo clicked */
            logoClicked(logo)

            button.setOnClickListener {
                viewModel.actionButtonClicked()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}