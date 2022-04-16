package com.entin.presentation.screens.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.entin.core.util.scrollLeft
import com.entin.core.util.scrollRight
import com.entin.presentation.databinding.ItemInfoArticleBinding
import com.entin.presentation.model.ArticleDomainModel
import com.entin.presentation.screens.list.adapter.slider.ArticleImageSliderAdapter
import kotlin.math.abs

class ArticlesAdapter(
    private val onClick: (ArticleDomainModel) -> Unit,
) : ListAdapter<ArticleDomainModel, ArticlesAdapter.ArticleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInfoArticleBinding.inflate(inflater, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(
        private val view: ItemInfoArticleBinding
    ) : RecyclerView.ViewHolder(view.root) {
        fun bind(item: ArticleDomainModel) {
            view.apply {
                root.setOnClickListener {
                    onClick(item)
                }
                /**
                 * Title
                 */
                title.text = item.title

                /**
                 * Image slider
                 */
                slider.apply {
                    adapter = ArticleImageSliderAdapter(item.images) { onClick(item) }
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
                }

                /**
                 * Right & Left clicks
                 */
                itemInfoSliderArrowRight.setOnClickListener {
                    slider.scrollRight()
                }

                itemInfoSliderArrowLeft.setOnClickListener {
                    slider.scrollLeft()
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ArticleDomainModel>() {
        override fun areItemsTheSame(oldItem: ArticleDomainModel, newItem: ArticleDomainModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ArticleDomainModel, newItem: ArticleDomainModel) =
            oldItem == newItem
    }
}