package com.entin.presentation.screens.main.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.entin.presentation.R
import com.entin.presentation.databinding.ItemMainScreenSliderBinding
import com.entin.presentation.model.SliderItemModel

class ViewPagerAdapter(
    private var onClickListener: (SliderItemModel) -> Unit,
    private val list: List<SliderItemModel>,
) : RecyclerView.Adapter<ViewPagerAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMainScreenSliderBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(list[position])
        holder.setOnClick(onClickListener, list[position])
    }

    class SliderViewHolder(private val view: ViewBinding) : RecyclerView.ViewHolder(view.root) {
        private val image = view.root.findViewById<ImageView>(R.id.view_pager_item_image)
        fun bind(sliderItem: SliderItemModel) {
            Glide.with(view.root.context).load(sliderItem.http)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(RoundedCorners(40))
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .fallback(R.drawable.no_img)
                .into(image)
        }

        fun setOnClick(
            onClickListener: (SliderItemModel) -> Unit,
            sliderItemModel: SliderItemModel
        ) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                view.root.setOnClickListener {
                    onClickListener(sliderItemModel)
                }
            }
        }
    }
}