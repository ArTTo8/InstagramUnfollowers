package com.artto.unfollowers.ui.login

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.unfollowers.ui.base.BaseView

interface LoginView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun navigateToMain()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showInputs(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLoginFailed()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun animateLogo(animate: Boolean)

}