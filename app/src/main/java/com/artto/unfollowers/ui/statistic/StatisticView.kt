package com.artto.unfollowers.ui.statistic

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import com.artto.unfollowers.ui.base.BaseView

interface StatisticView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(data: List<StatisticEntity>)

}