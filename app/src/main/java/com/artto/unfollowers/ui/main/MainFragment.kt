package com.artto.unfollowers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.unfollowers.R
import com.artto.unfollowers.ui.base.BaseFragment
import com.artto.unfollowers.ui.menu.MenuDialogFragment
import com.artto.unfollowers.utils.*
import com.artto.unfollowers.utils.extension.addOnTabSelectedListener
import com.artto.unfollowers.utils.extension.setOnQueryTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = inject<MainPresenter>().value

    private lateinit var recyclerAdapter: UsersRecyclerAdapter

    private lateinit var interstitialAd: InterstitialAd
    private var adRequest = AdRequest.Builder().addTestDevice("1DFA9DCEA0A8B3E80023BD68C54CF544").build()
    private val adListener = object : AdListener() {
        override fun onAdClosed() {
            interstitialAd.loadAd(adRequest)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(tl_groups) {
            addTab(newTab().setText(R.string.unfollowers).setTag(TabTag.TAG_UNFOLLOWERS))
            addTab(newTab().setText(R.string.followers).setTag(TabTag.TAG_FOLLOWERS))
            addTab(newTab().setText(R.string.following).setTag(TabTag.TAG_FOLLOWING))
            addOnTabSelectedListener { (it as? TabTag)?.let { presenter.onTabSelected(it) } }
        }

        recyclerAdapter = UsersRecyclerAdapter(presenter, Glide.with(this))
        rv_users.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(30)
            adapter = recyclerAdapter

            (layoutManager as? LinearLayoutManager)?.let {
                addOnScrollListener(UnderPositionScrollListener(it, 15) {
                    fab_up.visibility = if (it) View.VISIBLE else View.GONE
                })
            }
        }

        with(sv_search) {
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    b_toolbar_username.visibility = View.GONE
                else {
                    b_toolbar_username.visibility = View.VISIBLE
                    sv_search.isIconified = true
                }
            }

            setOnQueryTextChangedListener {
                presenter.onFilter(it)
                true
            }
        }

        b_toolbar_username.setOnClickListener { createFragment<MenuDialogFragment>().show(childFragmentManager, null) }

        fab_up.setOnClickListener { rv_users.smoothScrollToPosition(0) }

        interstitialAd = InterstitialAd(context)
        with(interstitialAd) {
            adUnitId = Ads.FOLLOW_UNFOLLOW_AD_ID
            adListener = this@MainFragment.adListener
            loadAd(adRequest)
        }

        tl_groups.getTabAt(savedInstanceState?.getInt(KEY_TAB_POSITION, 0) ?: 0)?.select()
    }

    override fun setUsername(username: String) {
        b_toolbar_username.text = username
    }

    override fun showProgressBar(show: Boolean) {
        activity?.progress_bar?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun updateData(oldList: List<InstagramUserSummary>, newList: List<InstagramUserSummary>) =
            DiffUtil.calculateDiff(UserDiffUtil(oldList, newList))
                    .dispatchUpdatesTo(recyclerAdapter)

    override fun notifyDataSetChanged() = recyclerAdapter.notifyDataSetChanged()

    override fun notifyItemChanged(position: Int) = recyclerAdapter.notifyItemChanged(position)

    override fun collapseSearchView() {
        sv_search.onActionViewCollapsed()
    }

    override fun showUserProfile(username: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, "http://instagram.com/_u/$username".toUri()))
        } catch (e: Exception) {
        }
    }

    override fun showAd() {
        if (this::interstitialAd.isInitialized && interstitialAd.isLoaded)
            interstitialAd.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_TAB_POSITION, tl_groups.selectedTabPosition)
    }

    override fun onDestroyView() {
        rv_users.adapter = null
        super.onDestroyView()
    }

    override fun getLayout() = R.layout.fragment_main

    companion object {
        private const val KEY_TAB_POSITION = "tab_position"
    }

}