package com.artto.unfollowers.ui.main.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artto.unfollowers.R
import com.artto.unfollowers.data.remote.InstagramUser
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.item_user.view.*

class UsersRecyclerAdapter(private val presenter: UsersAdapterPresenter,
                           private val itemPresenter: UsersItemPresenter,
                           private val requestManager: RequestManager) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false), itemPresenter, requestManager)

    override fun getItemCount() = presenter.getItemCount()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
            presenter.onBindViewHolder(holder, position)

    override fun onViewRecycled(holder: UserViewHolder) = holder.recycle()

}

interface UsersAdapterPresenter {
    fun getItemCount(): Int
    fun onBindViewHolder(view: UserItemView, position: Int)
}