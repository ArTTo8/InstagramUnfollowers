package com.artto.unfollowers.ui.main

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.unfollowers.ui.base.BaseView
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary

interface MainView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showProgressBar(show: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setUsername(username: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun notifyDataSetChanged()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun notifyItemChanged(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun updateData(oldList: List<InstagramUserSummary>, newList: List<InstagramUserSummary>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showUserProfile(username: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun collapseSearchView()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showAd()

}