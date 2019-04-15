package com.artto.unfollowers.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.unfollowers.R
import com.artto.unfollowers.ui.base.BaseFragment
import com.artto.unfollowers.ui.main.recycler.UsersRecyclerAdapter
import com.artto.unfollowers.ui.menu.MenuDialogFragment
import com.artto.unfollowers.ui.rate.RateDialogFragment
import com.artto.unfollowers.utils.*
import com.artto.unfollowers.utils.extension.addOnTabSelectedListener
import com.artto.unfollowers.utils.extension.setOnQueryTextChangedListener
import com.bumptech.glide.Glide
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
    private var adRequest = AdRequest.Builder().build()
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
            getTabAt(savedInstanceState?.getInt(KEY_TAB_POSITION, 0) ?: 0)?.select()
        }

        recyclerAdapter = UsersRecyclerAdapter(presenter, presenter, presenter, Glide.with(this))
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

        with(srl_users) {
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent))
            setOnRefreshListener {
                isRefreshing = false
                presenter.onRefresh(tl_groups.getTabAt(tl_groups.selectedTabPosition)?.tag as? TabTag)
            }

        }

        with(sv_search) {
            setOnQueryTextFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    iv_toolbar_user.visibility = View.GONE
                else {
                    iv_toolbar_user.visibility = View.VISIBLE
                    sv_search.isIconified = true
                }
            }

            setOnQueryTextChangedListener {
                presenter.onFilter(it)
                true
            }
        }

        iv_toolbar_user.setOnClickListener { createFragment<MenuDialogFragment>().show(childFragmentManager, null) }

        fab_up.setOnClickListener { rv_users.smoothScrollToPosition(0) }

        interstitialAd = InterstitialAd(context)
        with(interstitialAd) {
            adUnitId = Ads.FOLLOW_UNFOLLOW_AD_ID
            adListener = this@MainFragment.adListener
            loadAd(adRequest)
        }
    }

    override fun setUserPhoto(url: String) {
        Glide.with(this)
                .load(url)
                .transition(withCrossFade())
                .circleCrop()
                .placeholder(R.drawable.ic_person)
                .into(iv_toolbar_user)
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

    override fun showRateDialog() = createFragment<RateDialogFragment>().show(childFragmentManager, null)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        tl_groups?.let { outState.putInt(KEY_TAB_POSITION, it.selectedTabPosition) }
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