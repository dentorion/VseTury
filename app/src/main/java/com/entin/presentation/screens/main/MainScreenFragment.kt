package com.entin.presentation.screens.main

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.entin.core.base.BaseFragment
import com.entin.core.extension.renderResult
import com.entin.data.local.Cities
import com.entin.presentation.R
import com.entin.presentation.databinding.MainFragmentBinding
import com.entin.presentation.model.SliderItemModel
import com.entin.presentation.screens.main.slider.ViewPagerAdapter
import com.entin.presentation.screens.main.spinner.CityAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
class MainScreenFragment : BaseFragment() {

    override val viewModel by viewModels<MainViewModel>()
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var cities: Cities
    private var adapterSpinnerCityFrom: CityAdapter? = null
    private var adapterSpinnerCityTo: CityAdapter? = null
    private val dotsForSliderArray = mutableListOf<ImageView>()
    private var sliderItems = listOf<SliderItemModel>()
    private val delaySlider = 3500L
    private var sliderJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinnerAdapters()
        setupCitiesSpinners()
        observeViewState()
        setupOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        observeSliderItems()
    }

    /** City spinner Adapters */
    private fun setupSpinnerAdapters() {
        adapterSpinnerCityTo = CityAdapter(cities.getForeignCities())
        adapterSpinnerCityFrom = CityAdapter(cities.getPolandCities())
    }

    /** Search. City spinners from and to */
    private fun setupCitiesSpinners() {
        binding.apply {
            /** Spinner city from */
            mainSpinnerCityFrom.apply {
                adapter = adapterSpinnerCityFrom
                setBackgroundResource(R.drawable.main_search_city_spinner_background)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        viewModel.setCityFromPosition(p2)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }
            /** Spinner city to */
            mainSpinnerCityTo.apply {
                adapter = adapterSpinnerCityTo
                setBackgroundResource(R.drawable.main_search_city_spinner_background)
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        viewModel.setCityToPosition(p2)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }
        }
    }

    /** ViewState observer (LiveData) */
    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewResult ->
            renderResult(root = binding.root, viewResult = viewResult) { viewState ->
                setupCalendarDates(viewState.dateFrom, viewState.dateTo)
                setupDatePickers(viewState.dateFromCalendar, viewState.dateToCalendar)
                installCitiesPosition(viewState.cityFromPosition, viewState.cityToPosition)
            }
        }
    }

    /** Slider observer */
    private fun observeSliderItems() {
        viewModel.sliderItems.observe(viewLifecycleOwner) { sliders ->
            setupSliderItems(sliders)
        }
    }

    /** Setup onClickListeners */
    private fun setupOnClickListeners() {
        binding.apply {
            // Search button
            mainSearchBtn.setOnClickListener {
                viewModel.searchTours(
                    cityFromId = adapterSpinnerCityFrom?.getItem(mainSpinnerCityFrom.selectedItemPosition)?.id
                        ?: Int.MAX_VALUE,
                    cityToId = adapterSpinnerCityTo?.getItem(mainSpinnerCityTo.selectedItemPosition)?.id
                        ?: Int.MAX_VALUE,
                )
            }

            // Favourite icon click
            mainCircleBtnHeart.setOnClickListener {
                viewModel.navigateToFavouriteTours()
            }

            // Bell icon click
            mainCircleBtnBell.setOnClickListener {
                viewModel.navigateToUpcomingTours()
            }

            // Star icon click
            mainCircleBtnStar.setOnClickListener {
                viewModel.navigateToInfoArticlesList()
            }
        }
    }

    // observeViewState(). Setup title of buttons
    private fun setupCalendarDates(dateFromText: String, dateToText: String) {
        binding.apply {
            mainDateFrom.text = dateFromText
            mainDateTo.text = dateToText
        }
    }

    // observeViewState(). Date pickers from and to.
    private fun setupDatePickers(
        dateFromTriple: Triple<Int, Int, Int>,
        dateToTriple: Triple<Int, Int, Int>,
    ) {
        binding.apply {
            // Calendar From
            mainDateFrom.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    null,
                    dateFromTriple.first,
                    dateFromTriple.second.minus(1),
                    dateFromTriple.third,
                ).also { dialog ->
                    dialog.apply {
                        datePicker.minDate = Date().time
                        setOnDateSetListener { _, year, month, day ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.setDateFrom(year, month, day)
                                }
                            }
                        }
                        show()
                    }
                }
            }
            // Calendar To
            mainDateTo.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    null,
                    dateToTriple.first,
                    dateToTriple.second.minus(1),
                    dateToTriple.third,
                ).also { dialog ->
                    dialog.apply {
                        setOnDateSetListener { _, year, month, day ->
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.setDateTo(year, month, day)
                                }
                            }
                        }
                        show()
                    }
                }
            }
        }
    }

    // observeViewState(). Init previous city positions in spinners.
    private fun installCitiesPosition(cityFromPosition: Int, cityToPosition: Int) {
        binding.mainSpinnerCityFrom.setSelection(cityFromPosition)
        binding.mainSpinnerCityTo.setSelection(cityToPosition)
    }

    // observeSliderItems(). Get slider items
    private fun setupSliderItems(newSliderItems: List<SliderItemModel>) {
        if (newSliderItems.isNotEmpty()) {
            sliderItems = newSliderItems
            setupSliderDots()
            setupSlider()
            autoScrollSlider()
        }
    }

    // setupSliderItems(). Setup dots above slider
    private fun setupSliderDots() {
        if (dotsForSliderArray.isEmpty()) {
            sliderItems.forEach { _ ->
                val dotView = ImageView(requireActivity()).apply {
                    setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity().applicationContext,
                            R.drawable.decoration_slider_dot_black_false
                        )
                    )
                }
                val params = LinearLayout.LayoutParams(
                    resources.getDimension(R.dimen.main_slider_dots_size).toInt(),
                    resources.getDimension(R.dimen.main_slider_dots_size).toInt()
                ).apply {
                    setMargins(
                        resources.getDimension(R.dimen.main_slider_dots_margin).toInt(), 0,
                        resources.getDimension(R.dimen.main_slider_dots_margin).toInt(), 0
                    )
                }
                binding.mainLinearDots.addView(dotView, params)
                dotsForSliderArray.add(dotView)
            }

            oneSliderDotOn(0)
        }
    }

    // setupSliderItems(). Slider. Magic numbers for animation scale
    private fun setupSlider() {
        binding.mainSlider.apply {
            adapter = ViewPagerAdapter(::sliderClickListener, sliderItems)
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r: Float = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
            })

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    viewModel.positionSliderSet(position)
                    setAllSliderDotsOff()
                    oneSliderDotOn(position)
                }
            })
        }
    }

    // setupSliderItems(). AutoScroll Slider
    private fun autoScrollSlider() {
        sliderJob = lifecycleScope.launchWhenResumed {
            delay(delaySlider)
            binding.mainSlider.apply {
                val numberOfItems = this.adapter?.itemCount ?: 0
                val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
                if (viewModel.positionSliderGet() == lastIndex) {
                    viewModel.positionSliderZero()
                    setCurrentItem(viewModel.positionSliderGet(), false)
                } else {
                    viewModel.positionSliderPlus()
                    setCurrentItem(viewModel.positionSliderGet(), true)
                }
                autoScrollSlider()
            }
        }
    }

    // setupSlider(). Slider click listener
    private fun sliderClickListener(sliderItemModel: SliderItemModel) {
        viewModel.sliderItemClicked(sliderItemModel)
    }

    // setupSlider(). Set dots "on"
    private fun oneSliderDotOn(position: Int) {
        dotsForSliderArray[position].setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity().applicationContext,
                R.drawable.decoration_slider_dot_yellow_true
            )
        )
    }

    // setupSlider(). Set dots "off"
    private fun setAllSliderDotsOff() {
        dotsForSliderArray.forEach {
            it.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity().applicationContext,
                    R.drawable.decoration_slider_dot_black_false
                )
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sliderJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        dotsForSliderArray.clear()
        adapterSpinnerCityTo = null
        adapterSpinnerCityFrom = null
    }
}