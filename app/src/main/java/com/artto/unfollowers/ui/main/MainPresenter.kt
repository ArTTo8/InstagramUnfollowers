package com.artto.unfollowers.ui.main

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.analytics.AnalyticsManager
import com.artto.unfollowers.data.remote.InstagramUser
import com.artto.unfollowers.data.repository.InstagramRepository
import com.artto.unfollowers.data.repository.UserRepository
import com.artto.unfollowers.ui.base.BasePresenter
import com.artto.unfollowers.ui.main.recycler.*
import com.artto.unfollowers.utils.TabTag
import com.artto.unfollowers.utils.extension.withProgress
import com.artto.unfollowers.utils.extension.withSchedulers
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MainPresenter(private val instagramRepository: InstagramRepository,
                    private val userRepository: UserRepository,
                    private val analyticsManager: AnalyticsManager) :
        BasePresenter<MainView>(),
        UsersAdapterPresenter,
        HeaderItemPresenter,
        UsersItemPresenter {

    private var currentTabTag = TabTag.TAG_UNFOLLOWERS
    private var query = ""
    private val items = ArrayList<InstagramUser>()
        get() = if (query.isBlank()) field else ArrayList(field.filter { it.username.startsWith(query, true) })


    override fun onFirstViewAttach() {
        viewState.setUserPhoto(instagramRepository.userPhotoUrl)

        loadUnfollowers()
        if (userRepository.needToShowRateDialog())
            viewState.showRateDialog()
    }

    private fun loadUnfollowers() {
        instagramRepository.getUnfollowers()
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .subscribeBy(
                        onComplete = { onTabSelected(TabTag.TAG_UNFOLLOWERS) },
                        onError = { onTabSelected(TabTag.TAG_UNFOLLOWERS) })
                .addTo(compositeDisposable)
    }

    fun onFilter(query: String) {
        val oldList = ArrayList(items)
        this.query = query
        viewState.updateData(oldList, items)
    }

    fun onTabSelected(tag: TabTag) {
        onFilter("")
        viewState.collapseSearchView()

        items.clear()
        items.addAll(instagramRepository.users.filter {
            when (tag) {
                TabTag.TAG_UNFOLLOWERS -> it.isFollowedByUser && !it.isFollower
                TabTag.TAG_FOLLOWERS -> it.isFollower
                TabTag.TAG_FOLLOWING -> it.isFollowedByUser
            }
        })
        currentTabTag = tag

        viewState.notifyDataSetChanged()
    }

    override fun onUnfollowClicked(position: Int) {
        instagramRepository.unfollow(items[position].pk)
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .doOnSubscribe { if (userRepository.needToShowAd()) viewState.showAd() }
                .doOnSubscribe { analyticsManager.logEvent(AnalyticsManager.Event.UNFOLLOW) }
                .subscribeBy(onError = { it.printStackTrace() })
                .addTo(compositeDisposable)

        val oldList = ArrayList(items)
        items.remove(items[position])
        viewState.updateData(oldList, items)
    }

    override fun onMultipleUnfollowClicked() {
        val count = 20
        val unfollowList = items.subList(0, count)
        Observable.fromIterable(unfollowList)
                .flatMap { instagramRepository.unfollow(it.pk).andThen(Observable.just(it)) }
                .doOnNext { analyticsManager.logEvent(AnalyticsManager.Event.UNFOLLOW) }
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .doOnSubscribe { viewState.showAd() }
                .subscribeBy(
                        onComplete = {
                            val oldList = ArrayList(items)
                            items.removeAll(items.subList(0, count))
                            viewState.updateData(oldList, items)
                        },
                        onError = {
                            it.printStackTrace()
                            loadUnfollowers()
                        })
                .addTo(compositeDisposable)
    }

    override fun onFollowClicked(position: Int) {
        instagramRepository.follow(items[position].pk)
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .doOnSubscribe { if (userRepository.needToShowAd()) viewState.showAd() }
                .doOnSubscribe { analyticsManager.logEvent(AnalyticsManager.Event.FOLLOW) }
                .subscribeBy(
                        onSuccess = {
                            if (it.status == "ok") {
                                items[position].isFollowedByUser = true
                                viewState.notifyItemChanged(position)
                            }
                        },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

    override fun onItemClicked(position: Int) = viewState.showUserProfile(items[position].username)

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) =
            if (position == 0 && currentTabTag == TabTag.TAG_UNFOLLOWERS)
                UsersRecyclerAdapter.ItemType.HEADER.value
            else
                UsersRecyclerAdapter.ItemType.USER.value

    override fun onBindViewHolder(view: UserItemView, position: Int) = view.setData(items[position])

}