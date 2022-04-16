package com.entin.presentation.screens.detail

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.entin.core.base.BaseFragment
import com.entin.core.extension.renderResult
import com.entin.core.util.showSnackbar
import com.entin.core.util.takeIfSuccess
import com.entin.presentation.R
import com.entin.presentation.databinding.DetailFragmentBinding
import com.entin.presentation.model.TourDomainModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailScreenFragment : BaseFragment() {

    override val viewModel by viewModels<DetailViewModel>()
    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private var currentTour: TourDomainModel? = null
    private var snackbar: Snackbar? = null
    private val citiesThroughArray = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)

        /** Animate bus */
        animation()
        /** Observe View State */
        observeDetailScreenViewState()
        /** Set onClickListeners */
        setupOnClickListeners()

        return binding.root
    }

    /**
     * Animation of bus
     */
    private fun animation() {
        ObjectAnimator.ofFloat(binding.detailCar, "translationX", 200f).apply {
            duration = 1000
            start()
        }
    }

    private fun observeDetailScreenViewState() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewResult ->
            renderResult(root = binding.root, viewResult = viewResult) { viewState ->
                /** Set current tour for clickListeners */
                currentTour = viewState.tourToShow
                /** Set all text fields with information */
                showDetailInformationAboutTour(viewState.tourToShow)
                /** Set favourite icon status of tour*/
                setFavouriteIcon(viewState.favouriteState)
                /** Show cities middle */
                showCitiesThrough()
            }
        }
    }

    /**
     * Cities between start point and finish city
     */
    private fun showCitiesThrough() {
        if (citiesThroughArray.isEmpty()) {
            currentTour?.citiesFromName?.forEachIndexed { index, cityName ->
                if (index > 0) {
                    val cityView = TextView(requireActivity()).also {
                        it.apply {
                            text = cityName
                            isAllCaps = true
                            textSize = 12F
                            setTextColor(resources.getColor(R.color.white, resources.newTheme()))
                            background = ContextCompat.getDrawable(
                                requireActivity().applicationContext,
                                R.drawable.item_tour_search_background_rectangle
                            )
                            background.setTint(
                                resources.getColor(
                                    R.color.black,
                                    resources.newTheme()
                                )
                            )
                            setPadding(resources.getDimension(R.dimen.detail_cities_through_padding_left).toInt(),
                                resources.getDimension(R.dimen.detail_cities_through_padding_top).toInt(),
                                resources.getDimension(R.dimen.detail_cities_through_padding_right).toInt(),
                                resources.getDimension(R.dimen.detail_cities_through_padding_bottom).toInt())
                            setOnClickListener {
                                viewModel.toursCityFrom(currentTour!!.citiesFrom[index])
                            }
                        }
                    }

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 24, 0)
                    }

                    binding.detailLinearDots.addView(cityView, params)
                    citiesThroughArray.add(cityView)
                }
            }
        }
    }

    private fun showDetailInformationAboutTour(tour: TourDomainModel) {
        binding.apply {
            detailContactName.text = tour.contactName
            detailContactPhone.text = tour.contactPhone
            detailDescription.text = tour.description
            dateFrom.text = tour.dateFrom
            dateTo.text = tour.dateTo
            detailPrice.text = tour.price.toString()
            detailTitle.text = tour.title
            detailCityFrom.text = tour.citiesFromName[0]
            detailCityTo.text = tour.cityToName
            detailLinearDots.isVisible = tour.citiesFromName.size > 1
            detailLabelThrough.isVisible = tour.citiesFromName.size > 1
        }
    }

    /**
     * Setup favourite icon
     */
    private fun setFavouriteIcon(mode: Boolean) {
        if (mode) {
            binding.detailHeart.imageTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.real_red
            )
        } else {
            binding.detailHeart.imageTintList = ContextCompat.getColorStateList(
                requireContext(),
                R.color.sunset_dark
            )
        }
    }

    /**
     * Setup onClickListeners
     */
    private fun setupOnClickListeners() {
        binding.apply {
            /** Logo clicked */
            logoClicked(logo)

            /** Like clicked */
            detailHeartBtn.setOnClickListener { view ->
                viewModel.changeFavouriteStateOfTour()

                snackbar?.dismiss()
                snackbar = if (viewModel.viewState.value.takeIfSuccess()?.favouriteState == true) {
                    view.showSnackbar(R.string.list_favourite_delete).also {
                        it.show()
                    }
                } else {
                    view.showSnackbar(R.string.list_favourite_add).also {
                        it.show()
                    }
                }
            }

            /** City From */
            detailCityFrom.setOnClickListener {
                currentTour?.let {
                    viewModel.toursCityFrom(it.citiesFrom[0])
                }
            }

            /** City To */
            detailCityTo.setOnClickListener {
                currentTour?.let {
                    viewModel.toursCityTo(it.cityToId)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentTour = null
        _binding = null
        citiesThroughArray.clear()
        snackbar?.dismiss()
        snackbar = null
    }
}