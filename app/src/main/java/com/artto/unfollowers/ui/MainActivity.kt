package com.artto.unfollowers.ui

import android.os.Bundle
import com.appodeal.ads.Appodeal
import com.artto.unfollowers.R
import com.artto.unfollowers.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress_bar.setOnTouchListener { _, _ -> true }
        Appodeal.initialize(this, "8505e3f19980295b7942de03e2e2053c996764c997d1518e", Appodeal.INTERSTITIAL, false)
    }
}