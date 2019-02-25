package com.artto.instagramunfollowers.ui.statistic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.instagramunfollowers.data.local.db.entity.StatisticEntity
import com.artto.instagramunfollowers.ui.base.BaseView

interface StatisticView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<StatisticEntity>)

}