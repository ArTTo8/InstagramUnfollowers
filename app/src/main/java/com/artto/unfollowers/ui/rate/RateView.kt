package com.artto.unfollowers.ui.rate

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.unfollowers.ui.base.BaseView

interface RateView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun openGooglePlay()

}