package com.artto.instagramunfollowers.data.interactor

import com.artto.instagramunfollowers.data.repository.InstagramRepository

class InstagramInteractor(private val instagramRepository: InstagramRepository) {

    fun login(username: String, password: String) = instagramRepository.login(username, password)

}