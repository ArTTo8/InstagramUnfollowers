package com.artto.instagramunfollowers.ui.login

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.remote.InstagramRepository
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.extension.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter(private val instagramRepository: InstagramRepository) : BasePresenter<LoginView>() {

    override fun onFirstViewAttach() {
        with(instagramRepository.getUserData()) {
            if (first.isNotBlank() && second.isNotBlank())
                login(first, second)
            else
                viewState.showInputs(true)
        }
    }

    fun onLoginClicked(username: String, password: String) = login(username, password)

    private fun login(username: String, password: String) {
        instagramRepository.logIn(username, password)
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .doOnSubscribe { viewState.showInputs(false) }
                .subscribeBy(
                        onSuccess = { viewState.navigateToMain() },
                        onError = {
                            it.printStackTrace()
                            viewState.onLoginFailed()
                            viewState.showInputs(true)
                        })
                .addTo(compositeDisposable)
    }

}