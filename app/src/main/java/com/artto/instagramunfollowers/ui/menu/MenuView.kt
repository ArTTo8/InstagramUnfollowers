package com.artto.instagramunfollowers.ui.menu

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.instagramunfollowers.ui.base.BaseView

interface MenuView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToStatistic()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToLogin()

}