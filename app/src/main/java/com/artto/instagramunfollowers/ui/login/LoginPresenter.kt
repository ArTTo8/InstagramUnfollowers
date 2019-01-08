package com.artto.instagramunfollowers.ui.login

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.entity.User
import com.artto.instagramunfollowers.data.interactor.InstagramInteractor
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.rx.with
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@InjectViewState
class LoginPresenter(private val instagramInteractor: InstagramInteractor) : BasePresenter<LoginView>() {

    override fun onFirstViewAttach() {
        instagramInteractor.getUserSingle()
                .zipWith(Single.timer(2, TimeUnit.SECONDS)) { user: User, _: Long? -> user }
                .subscribeBy {
                    if (it.isAuthorized) viewState.navigateToMain()
                    else viewState.showInputs()
                }
                .addTo(compositeDisposable)
    }

    fun onLoginClicked(username: String, password: String) {
        instagramInteractor.login(username, password)
                .with(subscribeOn = Schedulers.io(), observeOn = AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgressBar(true) }
                .doOnEvent { _, _ -> viewState.showProgressBar(false) }
                .subscribeBy(
                        onSuccess = { viewState.navigateToMain() },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

}