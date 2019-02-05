package com.artto.instagramunfollowers.ui

import android.os.Bundle
import com.artto.instagramunfollowers.R
import com.artto.instagramunfollowers.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progress_bar.setOnTouchListener { _, _ -> true }
    }
}