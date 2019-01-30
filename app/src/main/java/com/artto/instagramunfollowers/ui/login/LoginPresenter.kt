package com.artto.instagramunfollowers.ui.login

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.InstagramRepository
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.withProgress
import com.artto.instagramunfollowers.utils.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class LoginPresenter(private val instagramRepository: InstagramRepository) : BasePresenter<LoginView>() {

    override fun onFirstViewAttach() {
        with(instagramRepository.getUserData()) {
            if (first.isNotBlank() && second.isNotBlank())
                onLoginClicked(first, second)
            else
                viewState.showInputs()
        }
    }

    fun onLoginClicked(username: String, password: String) = login(username, password)

    private fun login(username: String, password: String) {
        instagramRepository.login(username, password)
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .doOnError { viewState.showInputs() }
                .subscribeBy(
                        onSuccess = { viewState.navigateToMain() },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

}