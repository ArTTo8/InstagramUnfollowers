package com.artto.unfollowers.ui.menu

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.analytics.AnalyticsManager
import com.artto.unfollowers.data.remote.InstagramRepository
import com.artto.unfollowers.ui.base.BasePresenter
import com.artto.unfollowers.utils.extension.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

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