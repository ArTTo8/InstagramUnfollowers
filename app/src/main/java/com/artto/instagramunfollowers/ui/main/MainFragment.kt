package com.artto.instagramunfollowers.ui.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.instagramunfollowers.R
import com.artto.instagramunfollowers.ui.base.BaseFragment
import com.artto.instagramunfollowers.utils.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class MainFragment : BaseFragment(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = inject<MainPresenter>().value

    private lateinit var recyclerAdapter: UsersRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = UsersRecyclerAdapter(presenter)
        with(rv_users) {
            adapter = recyclerAdapter
            addOnScrollListener(EndlessRecyclerViewScrollListener(
                    layoutManager as LinearLayoutManager,
                    presenter::onLoadMore))
        }
    }

    override fun setUsername(username: String) {
        b_toolbar_username.text = username
    }

    override fun notifyDataSetChanged() = recyclerAdapter.notifyDataSetChanged()

    override fun getLayout() = R.layout.fragment_main

}