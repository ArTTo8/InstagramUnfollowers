package com.artto.instagramunfollowers.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artto.instagramunfollowers.R
import com.artto.instagramunfollowers.data.InstagramUser
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class UsersRecyclerAdapter(private val presenter: UsersAdapterPresenter) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount() = presenter.getItemCount()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = presenter.onBindViewHolder(holder, position)

}


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), UserItemView {

    override fun setData(user: InstagramUser, unfollowClickListener: (Int) -> Unit, followClickListener: (Int) -> Unit, itemClickListener: (Int) -> Unit) {
        with(itemView) {
            setOnClickListener { itemClickListener.invoke(adapterPosition) }

            Glide.with(this).load(user.profile_pic_url).into(iv_user_profile_image)
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

}


interface UsersAdapterPresenter {
    fun getItemCount(): Int
    fun onBindViewHolder(view: UserItemView, position: Int)
}


interface UserItemView {
    fun setData(user: InstagramUser, unfollowClickListener: (Int) -> Unit, followClickListener: (Int) -> Unit, itemClickListener: (Int) -> Unit)
}