package com.artto.unfollowers.ui.main

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
                           private val requestManager: RequestManager) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false), requestManager)

    override fun getItemCount() = presenter.getItemCount()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = presenter.onBindViewHolder(holder, position)

    override fun onViewRecycled(holder: UserViewHolder) {
        super.onViewRecycled(holder)
        holder.recycle()
    }

}


class UserViewHolder(itemView: View, private val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView), UserItemView {

    override fun setData(user: InstagramUser, unfollowClickListener: (Int) -> Unit, followClickListener: (Int) -> Unit, itemClickListener: (Int) -> Unit) {
        with(itemView) {
            setOnClickListener { itemClickListener.invoke(adapterPosition) }

            requestManager.load(user.profile_pic_url)
                    .transition(withCrossFade())
                    .transform(CircleCrop())
                    .override(100)
                    .into(iv_user_profile_image)

            tv_user_username.text = user.username

            b_user_action.apply {
                text = context.getText(if (user.isFollowedByUser) R.string.unfollow else R.string.follow)
                setBackgroundResource(if (user.isFollowedByUser) R.drawable.bg_user_unfollow_button else R.drawable.bg_user_follow_button)
                setOnClickListener {
                    if (user.isFollowedByUser)
                        unfollowClickListener.invoke(adapterPosition)
                    else
                        followClickListener.invoke(adapterPosition)
                }
            }
        }
    }

    override fun recycle() = requestManager.clear(itemView.iv_user_profile_image)

}


interface UsersAdapterPresenter {
    fun getItemCount(): Int
    fun onBindViewHolder(view: UserItemView, position: Int)
}


interface UserItemView {
    fun setData(user: InstagramUser, unfollowClickListener: (Int) -> Unit, followClickListener: (Int) -> Unit, itemClickListener: (Int) -> Unit)
    fun recycle()
}