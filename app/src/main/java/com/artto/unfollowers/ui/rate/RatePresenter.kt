package com.artto.unfollowers.ui.rate

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.analytics.AnalyticsManager
import com.artto.unfollowers.data.repository.UserRepository
import com.artto.unfollowers.ui.base.BasePresenter

@InjectViewState
class RatePresenter(private val analyticsManager: AnalyticsManager,
                    private val userRepository: UserRepository) : BasePresenter<RateView>() {

    fun onRateClicked() {
        analyticsManager.logEvent(AnalyticsManager.Event.RATE)
        userRepository.updateUserData(rateDialogShowed = true)
        viewState.openGooglePlay()
    }

}