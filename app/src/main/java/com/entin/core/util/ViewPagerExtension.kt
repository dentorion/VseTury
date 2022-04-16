package com.entin.core.util

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.scrollRight() {
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1
    setCurrentItem(nextItem, true)
}

fun ViewPager2.scrollLeft() {
    val numberOfItems = adapter?.itemCount ?: 0
    val firstIndex = if (numberOfItems > 0) 0 else 0
    val prevItem = if (currentItem == firstIndex) numberOfItems - 1 else currentItem - 1
    setCurrentItem(prevItem, true)
}