package com.artto.unfollowers.ui.main.recycler

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.artto.unfollowers.R
import com.artto.unfollowers.data.remote.InstagramUser
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_user.view.*

class UserViewHolder(itemView: View,
                     private val presenter: UsersItemPresenter,
                     private val requestManager: RequestManager) : RecyclerView.ViewHolder(itemView), UserItemView {

    init {
        with(itemView) {
            setOnClickListener { presenter.onItemClicked(adapterPosition) }
        }
    }

    override fun setData(user: InstagramUser) {
        with(itemView) {
            requestManager.load(user.profile_pic_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .transform(CircleCrop())
                    .override(100)
                    .into(iv_user_profile_image)

            tv_user_username.text = user.username

            b_user_action.apply {
                text = context.getText(if (user.isFollowedByUser) R.string.unfollow else R.string.follow)
                setBackgroundResource(if (user.isFollowedByUser) R.drawable.bg_user_unfollow_button else R.drawable.bg_user_follow_button)
                setOnClickListener {
                    if (user.isFollowedByUser)
                        presenter.onUnfollowClicked(adapterPosition)
                    else
                        presenter.onFollowClicked(adapterPosition)
                }
            }
        }
    }

    override fun recycle() = requestManager.clear(itemView.iv_user_profile_image)

}

interface UsersItemPresenter {
    fun onUnfollowClicked(position: Int)
    fun onFollowClicked(position: Int)
    fun onItemClicked(position: Int)
}

interface UserItemView {
    fun setData(user: InstagramUser)
    fun recycle()
}