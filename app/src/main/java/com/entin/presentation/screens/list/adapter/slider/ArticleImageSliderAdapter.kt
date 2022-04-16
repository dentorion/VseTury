package com.entin.presentation.screens.list.adapter.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.entin.presentation.R
import com.entin.presentation.databinding.ItemArticleSliderBinding

class ArticleImageSliderAdapter(
    private val list: List<String>,
    private val isRoundedCorner: Boolean = true,
    private val onClick: (() -> Unit)? = null,
) : RecyclerView.Adapter<ArticleImageSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemArticleSliderBinding.inflate(inflater, parent, false)
        return SliderViewHolder(binding, isRoundedCorner)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(list[position])
        onClick?.apply {
            holder.setOnClick(this)
        }
    }

    class SliderViewHolder(
        private val view: ViewBinding,
        isRoundedCorner: Boolean
    ) : RecyclerView.ViewHolder(view.root) {
        private val image = view.root.findViewById<ImageView>(R.id.article_item_image)
        private val roundCornerValue = if (isRoundedCorner) 80 else 1

        fun bind(sliderItem: String) {
            Glide.with(view.root.context).load(sliderItem)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(RoundedCorners(roundCornerValue))
                .transform()
                .placeholder(R.drawable.slider_item_no_img)
                .into(image)
        }

        fun setOnClick(
            onClickListener: () -> Unit,
        ) {
            val position = absoluteAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                view.root.setOnClickListener {
                    onClickListener()
                }
            }
        }
    }
}