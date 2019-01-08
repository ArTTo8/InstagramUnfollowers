package com.artto.instagramunfollowers.ui.main

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.instagramunfollowers.ui.base.BaseView

interface MainView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setUsername(username: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun notifyDataSetChanged()

}