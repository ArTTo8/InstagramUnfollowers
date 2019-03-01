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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(tl_groups) {
            addTab(newTab().setText(R.string.unfollowers).setTag(TabTag.TAG_UNFOLLOWERS))
            addTab(newTab().setText(R.string.followers).setTag(TabTag.TAG_FOLLOWERS))
            addTab(newTab().setText(R.string.following).setTag(TabTag.TAG_FOLLOWING))
            addOnTabSelectedListener { (it as? TabTag)?.let { presenter.onTabSelected(it) } }
        }

        recyclerAdapter = UsersRecyclerAdapter(presenter)
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

        sv_search.setOnQueryTextChangedListener {
            presenter.onFilter(it)
            true
        }

        b_toolbar_username.setOnClickListener { createFragment<MenuDialogFragment>().show(childFragmentManager, null) }

        fab_up.setOnClickListener { rv_users.smoothScrollToPosition(0) }
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

    override fun onDestroyView() {
        rv_users.adapter = null
        super.onDestroyView()
    }

    override fun getLayout() = R.layout.fragment_main

}