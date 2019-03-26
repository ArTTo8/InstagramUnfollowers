package com.artto.unfollowers.ui.statistic

import com.arellomobile.mvp.InjectViewState
import com.artto.unfollowers.data.local.db.repository.StatisticRepository
import com.artto.unfollowers.data.repository.InstagramRepository
import com.artto.unfollowers.ui.base.BasePresenter
import com.artto.unfollowers.utils.extension.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class StatisticPresenter(private val statisticRepository: StatisticRepository,
                         private val instagramRepository: InstagramRepository) : BasePresenter<StatisticView>() {

    override fun onFirstViewAttach() {
        statisticRepository.getAll(instagramRepository.getUserId())
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            if (it.isNotEmpty())
                                viewState.showData(it)
                        },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

}