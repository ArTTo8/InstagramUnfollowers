package com.artto.unfollowers.ui.menu

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.data.remote.InstagramRepository
import com.artto.unfollowers.ui.base.BasePresenter
import com.artto.unfollowers.utils.extension.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MenuPresenter(private val instagramRepository: InstagramRepository) : BasePresenter<MenuView>() {

    fun onStatisticClicked() {
        viewState.navigateToStatistic()
    }

    fun onLogOutClicked() {
        instagramRepository.logOut()
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .subscribeBy(
                        onError = { viewState.navigateToLogin() },
                        onComplete = { viewState.navigateToLogin() })
    }

}