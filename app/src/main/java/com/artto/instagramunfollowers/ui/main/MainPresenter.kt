package com.artto.instagramunfollowers.ui.main

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.InstagramRepository
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.withProgress
import com.artto.instagramunfollowers.utils.withSchedulers
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class MainPresenter(private val instagramRepository: InstagramRepository) : BasePresenter<MainView>(), UsersAdapterPresenter {

    private var query = ""
    private val items = ArrayList<InstagramUserSummary>()
        get() = if (query.isBlank()) field else ArrayList(field.filter { it.username.startsWith(query, true) })


    override fun onFirstViewAttach() {
        instagramRepository.user
                .subscribe { viewState.setUsername(it.username) }
                .addTo(compositeDisposable)

        loadUnfollowers()
    }

    private fun loadUnfollowers() {
        instagramRepository.getUnfollowers()
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .subscribeBy(
                        onSuccess = {
                            items.addAll(it)
                            viewState.notifyDataSetChanged()
                        },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

    private fun unfollow(userId: Long) {
        instagramRepository.unfollow(userId)
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .withProgress(viewState::showProgressBar)
                .subscribeBy(onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

    fun onFilter(query: String) {
        this.query = query
        viewState.notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(view: UserItemView, position: Int) = with(items[position]) {
        view.setData(profile_pic_url, username) {
            unfollow(pk)
            items.remove(this)
            viewState.notifyDataSetChanged()
        }
    }

}