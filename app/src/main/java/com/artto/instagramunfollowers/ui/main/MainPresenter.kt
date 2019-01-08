package com.artto.instagramunfollowers.ui.main

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.entity.User
import com.artto.instagramunfollowers.data.interactor.InstagramInteractor
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.rx.with
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MainPresenter(private val instagramInteractor: InstagramInteractor) : BasePresenter<MainView>(), UsersAdapterPresenter {

    private var isAlreadyLoading = false
    private var hasNext = true
    private var cursor = ""
    private val count = 20

    private lateinit var user: User
    private val list = ArrayList<User>()

    override fun onFirstViewAttach() {
        instagramInteractor.getUserSingle()
                .subscribeBy {
                    user = it
                    viewState.setUsername(it.userName)
                    loadData()
                }
                .addTo(compositeDisposable)
    }

    private fun loadData() = instagramInteractor.getUserFollowers(user.id, cursor, count)
            .with(subscribeOn = Schedulers.io(), observeOn = AndroidSchedulers.mainThread())
            .doOnSubscribe { isAlreadyLoading = true }
            .doOnEvent { _, _ -> isAlreadyLoading = false }
            .subscribeBy(
                    onSuccess = {
                        list.addAll(it.items)
                        viewState.notifyDataSetChanged()
                        cursor = it.endCursor
                        hasNext = it.hasNextPage
                    },
                    onError = {})
            .addTo(compositeDisposable)

    fun onLoadMore() {
        if (hasNext && !isAlreadyLoading) loadData()
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(view: UserItemView, position: Int) = with(list[position]) {
        view.setData(avatarThumbnailUrl, userName) {

        }
    }

}