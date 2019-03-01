package com.artto.unfollowers.utils

import androidx.recyclerview.widget.DiffUtil
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary

class UserDiffUtil(private val oldList: List<InstagramUserSummary>, private val newList: List<InstagramUserSummary>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].pk == newList[newItemPosition].pk

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition] == newList[newItemPosition]

}