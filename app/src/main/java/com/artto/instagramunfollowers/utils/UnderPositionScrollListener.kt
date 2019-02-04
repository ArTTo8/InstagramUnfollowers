package com.artto.instagramunfollowers.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UnderPositionScrollListener(private val layoutManager: LinearLayoutManager, private val position: Int, private val listener: (isUnder: Boolean) -> Unit) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0 && position <= layoutManager.findFirstVisibleItemPosition())
            listener.invoke(true)
        else if (dy < 0 && position >= layoutManager.findFirstVisibleItemPosition())
            listener.invoke(false)
    }
}