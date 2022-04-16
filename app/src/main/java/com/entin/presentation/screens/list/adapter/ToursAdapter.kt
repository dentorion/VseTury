package com.entin.presentation.screens.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entin.presentation.databinding.ItemTourSearchBinding
import com.entin.presentation.model.TourDomainModel

class ToursAdapter(
    private val onClick: (TourDomainModel) -> Unit,
) : ListAdapter<TourDomainModel, ToursAdapter.TourViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTourSearchBinding.inflate(inflater, parent, false)
        return TourViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TourViewHolder(
        private val view: ItemTourSearchBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: TourDomainModel) {
            view.apply {
                root.setOnClickListener {
                    onClick(item)
                }
                itemCityFrom.text = item.citiesFromName[0]
                itemCityTo.text = item.cityToName
                itemDateFrom.text = item.dateFrom
                itemDateTo.text = item.dateTo
                itemTitleTour.text = item.title
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TourDomainModel>() {
        override fun areItemsTheSame(oldItem: TourDomainModel, newItem: TourDomainModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TourDomainModel, newItem: TourDomainModel) =
            oldItem == newItem
    }
}