package com.entin.presentation.screens.main.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.entin.presentation.R
import com.entin.presentation.databinding.CityHolderSpinnerBinding
import com.entin.presentation.model.CityDomainModel

class CityAdapter(
    private val cityDomainModels: List<CityDomainModel>,
) : BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        return getDefaultView(p0, p1, p2, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDefaultView(position, convertView, parent, true)
    }

    private fun getDefaultView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        isDropDown: Boolean
    ): View {
        val binding = convertView?.tag as CityHolderSpinnerBinding? ?: createBinding(parent.context)
        val cityDomainModel: CityDomainModel = getItem(position)
        binding.apply {
            flagCountry.setImageResource(cityDomainModel.flag)
            mainCityName.text = cityDomainModel.name

            if (isDropDown) {
                binding.root.setBackgroundColor(
                    parent.context.resources.getColor(
                        R.color.citySpinnerBackground,
                        parent.context.theme
                    )
                )
            }
        }
        return binding.root
    }

    override fun getCount() = cityDomainModels.size

    override fun getItem(position: Int): CityDomainModel = cityDomainModels[position]

    override fun getItemId(position: Int) = cityDomainModels[position].id.toLong()

    private fun createBinding(context: Context?): CityHolderSpinnerBinding {
        val binding = CityHolderSpinnerBinding.inflate(LayoutInflater.from(context))
        binding.root.tag = binding
        return binding
    }
}