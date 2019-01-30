package com.artto.instagramunfollowers.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.artto.instagramunfollowers.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class UsersRecyclerAdapter(private val presenter: UsersAdapterPresenter) : RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
            UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount() = presenter.getItemCount()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) = presenter.onBindViewHolder(holder, position)

}


class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), UserItemView {

    override fun setData(imageUrl: String, username: String, unfollowClickListener: (Int) -> Unit) {
        with(itemView) {
            Glide.with(this).load(imageUrl).into(iv_user_profile_image)
            tv_user_username.text = username
            b_user_unfollow.setOnClickListener { unfollowClickListener.invoke(adapterPosition) }
        }
    }

}


interface UsersAdapterPresenter {
    fun getItemCount(): Int
    fun onBindViewHolder(view: UserItemView, position: Int)
}


interface UserItemView {
    fun setData(imageUrl: String, username: String, listener: (Int) -> Unit)
}