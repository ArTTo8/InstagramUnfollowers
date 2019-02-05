package com.artto.instagramunfollowers.utils.extension

import com.google.android.material.tabs.TabLayout

fun TabLayout.addOnTabSelectedListener(listener: (Any) -> Unit) {

    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.tag?.let { listener.invoke(it) }
        }

    })

}