package com.entin.presentation.screens.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.entin.core.base.BaseFragment
import com.entin.core.base.PARAMETER
import com.entin.core.extension.renderResult
import com.entin.core.util.showSnackbar
import com.entin.data.local.Cities
import com.entin.presentation.R
import com.entin.presentation.databinding.ListFragmentBinding
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.model.TourDomainModel
import com.entin.presentation.screens.list.adapter.ArticlesAdapter
import com.entin.presentation.screens.list.adapter.ItemTouchHelperCallback
import com.entin.presentation.screens.list.adapter.ToursAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListScreenFragment : BaseFragment() {

    override val viewModel by viewModels<ListViewModel>()
    private var _binding: ListFragmentBinding? = null
    private val binding get() = _binding!!
    private var toursAdapter: ToursAdapter? = null
    private var infoArticlesAdapter: ArticlesAdapter? = null
    private var parameters: ListFragmentAction? = null
    private var snackbar: Snackbar? = null
    private var errorTextsPair: Pair<String, String>? = null

    @Inject
    lateinit var cities: Cities

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(inflater, container, false)

        /** Get parameters of fragment */
        getFragmentArguments()
        /** Setup labels and adapter RecyclerView */
        setupElements()
        /** Observe ViewState from iewModel */
        observeViewState()
        /** Setup clickListeners */
        setupOnClickListeners()

        return binding.root
    }

    /**
     * Get arguments after navigation to this fragment
     */
    private fun getFragmentArguments() {
        parameters = try {
            requireArguments().get(PARAMETER) as ListFragmentAction
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Observe ViewState from ViewModel and
     * - set text value for header label
     * - init recyclerAdapter for tours or articles
     *
     * - set error text when using this page with recycler view in different cases
     */
    private fun setupElements() {
        parameters?.let { param ->
            when (param) {
                is ListFragmentAction.Search -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_common),
                        resources.getString(R.string.list_label_oops_button_common)
                    )
                    // Set header label
                    initHeaderLabel(
                        String.format(
                            resources.getString(R.string.list_label_search),
                            cities.getCityById(param.data.first.first)?.name,
                            cities.getCityById(param.data.first.second)?.name
                        )
                    )
                    // Init adapter for tour items
                    initRecyclerViewAndAdapter(isForTours = true)
                }
                is ListFragmentAction.ToursByCityFrom -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_common),
                        resources.getString(R.string.list_label_oops_button_common)
                    )
                    // Set header label
                    initHeaderLabel(
                        String.format(
                            resources.getString(R.string.list_label_city_from),
                            cities.getCityById(param.data)?.name,
                        )
                    )
                    // Init adapter for tour items
                    initRecyclerViewAndAdapter(isForTours = true)
                }
                is ListFragmentAction.ToursByCityTo -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_common),
                        resources.getString(R.string.list_label_oops_button_common)
                    )
                    // Set header label
                    initHeaderLabel(
                        String.format(
                            resources.getString(R.string.list_label_city_to),
                            cities.getCityById(param.data)?.name,
                        )
                    )
                    // Init adapter for tour items
                    initRecyclerViewAndAdapter(isForTours = true)
                }
                ListFragmentAction.FavouriteTours -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_favourite),
                        resources.getString(R.string.list_label_oops_button_info_articles)
                    )
                    // Set header label
                    initHeaderLabel(resources.getString(R.string.list_label_favourite))
                    // Init adapter for tour items
                    initRecyclerViewAndAdapter(isForTours = true)
                    // Touch Helper for deleting tours from favourites
                    initItemTouchHelper()
                }
                ListFragmentAction.Upcoming -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_upcoming),
                        resources.getString(R.string.list_label_oops_button_common)
                    )
                    // Set header label
                    initHeaderLabel(resources.getString(R.string.list_label_upcoming))
                    // Init adapter for tour items
                    initRecyclerViewAndAdapter(isForTours = true)
                }
                ListFragmentAction.Articles -> {
                    // Set texts for error case
                    errorTextsPair = Pair(
                        resources.getString(R.string.list_label_oops_info_articles),
                        resources.getString(R.string.list_label_oops_button_info_articles)
                    )
                    // Set header label
                    initHeaderLabel(resources.getString(R.string.list_label_info_articles))
                    // Init adapter for article items
                    initRecyclerViewAndAdapter(isForTours = false)
                }
            }
        }
    }

    /**
     * Observe ViewState
     */
    private fun observeViewState() {
        viewModel.listViewState.observe(viewLifecycleOwner) { viewResult ->
            renderResult(
                root = binding.root,
                viewResult = viewResult,
                errorText = errorTextsPair?.first,
                errorTextButton = errorTextsPair?.second,
            ) { viewState ->
                /** Fill Recycler View with Tours or Articles */
                fillRecyclerViewWithElements(viewState)
                /** Show / Hide elements based on viewState */
                visibilityOfViews(viewState)
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
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }
    }

    // Setup elements //////////////////////////////////////////////////////////////////////////////

    /**
     * Initial Recycler View for Tours
     */
    private fun initRecyclerViewAndAdapter(isForTours: Boolean) {
        with(binding) {
            listRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    requireContext().applicationContext,
                    RecyclerView.VERTICAL,
                    false
                )
                if (isForTours) {
                    if (toursAdapter == null) {
                        toursAdapter = ToursAdapter(::onItemClickTour)
                        adapter = toursAdapter
                    }
                } else {
                    if (infoArticlesAdapter == null) {
                        infoArticlesAdapter = ArticlesAdapter(::onItemClickArticle)
                        adapter = infoArticlesAdapter
                    }
                }
            }
        }
    }

    /**
     * On Tour clicked within [ToursAdapter]
     */
    private fun onItemClickTour(tourDomainModel: TourDomainModel) {
        parameters?.let {
            when (it) {
                is ListFragmentAction.Search -> {
                    viewModel.navigateToDetailScreenTour(tourDomainModel)
                }
                is ListFragmentAction.ToursByCityFrom -> {
                    viewModel.navigateToDetailScreenTour(tourDomainModel)
                }
                is ListFragmentAction.ToursByCityTo -> {
                    viewModel.navigateToDetailScreenTour(tourDomainModel)
                }
                ListFragmentAction.FavouriteTours -> {
                    viewModel.navigateToDetailScreenTour(tourDomainModel.id)
                }
                ListFragmentAction.Upcoming -> {
                    viewModel.navigateToDetailScreenTour(tourDomainModel)
                }
                else -> {}
            }
        }
    }

    private fun onItemClickArticle(infoArticle: ArticleDomainModel) {
        parameters?.let { param ->
            when (param) {
                ListFragmentAction.Articles -> {
                    viewModel.navigateToDetailScreenInfo(infoArticle)
                }
                else -> {}
            }
        }
    }

    // observeViewState ////////////////////////////////////////////////////////////////////////////

    /**
     * Fill Adapter with elements
     */
    private fun fillRecyclerViewWithElements(viewState: ListViewState) {
        infoArticlesAdapter?.submitList(viewState.articles)
        toursAdapter?.submitList(viewState.tours)
    }

    /**
     * Visibility of objects
     */
    private fun visibilityOfViews(viewState: ListViewState) {
        binding.listRecyclerView.isVisible =
            viewState.tours.isNotEmpty() || viewState.articles.isNotEmpty()
        binding.listHeaderLabel.isVisible =
            viewState.tours.isNotEmpty() || viewState.articles.isNotEmpty()
    }

    // reactOnFragmentArgument /////////////////////////////////////////////////////////////////////

    /**
     * Label under RecyclerView
     */
    private fun initHeaderLabel(value: String) {
        binding.listHeaderLabel.text = value
    }

    /**
     * Item Touch Helper for Favourite list of tours
     * for sliding guest to delete from favourites
     */
    private fun initItemTouchHelper() {
        ItemTouchHelper(
            ItemTouchHelperCallback(toursAdapter, ::swipeCallback)
        ).attachToRecyclerView(binding.listRecyclerView)
    }

    private fun swipeCallback(tour: TourDomainModel) {
        viewModel.changeFavouriteStatusOfTour(tour)
        snackbar?.dismiss()
        snackbar = binding.root.showSnackbar(R.string.list_favourite_delete).also {
            it.show()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    override fun onDestroyView() {
        super.onDestroyView()
        snackbar?.dismiss()
        snackbar = null
        toursAdapter = null
        infoArticlesAdapter = null
        parameters = null
        errorTextsPair = null
        _binding = null
    }
}