package com.artto.instagramunfollowers.ui.login

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.interactor.InstagramInteractor
import com.artto.instagramunfollowers.ui.base.BasePresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter(private val instagramInteractor: InstagramInteractor) : BasePresenter<LoginView>() {

    fun onLoginClicked(username: String, password: String) {
        instagramInteractor.getUserSingle()
                .flatMap {
                    if (!it.isAuthorized) instagramInteractor.login(username, password)
                    else Single.just(it)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgressBar(true) }
                .doOnEvent { _, _ -> viewState.showProgressBar(false) }
                .subscribeBy(
                        onSuccess = {
                            viewState.navigateToMain()
                        },
                        onError = {})
                .addTo(compositeDisposable)
    }

}