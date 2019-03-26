package com.artto.unfollowers.ui.menu

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.analytics.AnalyticsManager
import com.artto.unfollowers.data.repository.InstagramRepository
import com.artto.unfollowers.ui.base.BasePresenter

@InjectViewState
class MenuPresenter(private val instagramRepository: InstagramRepository,
                    private val analyticsManager: AnalyticsManager) : BasePresenter<MenuView>() {

    fun onStatisticClicked() {
        viewState.navigateToStatistic()
        analyticsManager.logEvent(AnalyticsManager.Event.STATISTIC_OPEN)
    }

    fun onLogOutClicked() {
        instagramRepository.logOut()
        viewState.navigateToLogin()
        analyticsManager.logEvent(AnalyticsManager.Event.LOGOUT)
    }

}