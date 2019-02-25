package com.artto.instagramunfollowers.ui.statistic

import com.arellomobile.mvp.InjectViewState
import com.artto.instagramunfollowers.data.local.db.repository.StatisticRepository
import com.artto.instagramunfollowers.ui.base.BasePresenter
import com.artto.instagramunfollowers.utils.extension.withSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

@InjectViewState
class StatisticPresenter(private val statisticRepository: StatisticRepository) : BasePresenter<StatisticView>() {

    override fun onFirstViewAttach() {
        statisticRepository.getAll()
                .withSchedulers(AndroidSchedulers.mainThread(), Schedulers.io())
                .subscribeBy(
                        onSuccess = { viewState.showData(it) },
                        onError = { it.printStackTrace() })
                .addTo(compositeDisposable)
    }

}