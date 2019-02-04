package com.artto.instagramunfollowers.ui.menu

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.InstagramRepository
import com.artto.instagramunfollowers.ui.base.BasePresenter

@InjectViewState
class MenuPresenter(private val instagramRepository: InstagramRepository) : BasePresenter<MenuView>() {

    fun onLogOutClicked() {
        instagramRepository.logOut()
        viewState.navigateToLogin()
    }

}