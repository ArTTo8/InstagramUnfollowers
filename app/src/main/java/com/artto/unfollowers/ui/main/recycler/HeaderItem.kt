package com.artto.unfollowers.ui.main.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_header.view.*

class HeaderViewHolder(itemView: View, presenter: HeaderItemPresenter) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.b_header_unfollow.setOnClickListener { presenter.onMultipleUnfollowClicked() }
    }

}

interface HeaderItemPresenter {
    fun onMultipleUnfollowClicked()
}