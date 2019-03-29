package com.artto.unfollowers.ui.main.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artto.unfollowers.R
import com.bumptech.glide.RequestManager

class UsersRecyclerAdapter(private val presenter: UsersAdapterPresenter,
                           private val headerItemPresenter: HeaderItemPresenter,
                           private val usersItemPresenter: UsersItemPresenter,
                           private val requestManager: RequestManager) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ItemType(val value: Int) { HEADER(0), USER(1) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ItemType.USER.value -> UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false), usersItemPresenter, requestManager)
        else -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false), headerItemPresenter)
    }

    override fun getItemCount() = presenter.getItemCount()

    override fun getItemViewType(position: Int) = presenter.getItemViewType(position)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) presenter.onBindViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is UserViewHolder) holder.recycle()
    }

}

interface UsersAdapterPresenter {
    fun getItemCount(): Int
    fun getItemViewType(position: Int): Int
    fun onBindViewHolder(view: UserItemView, position: Int)
}