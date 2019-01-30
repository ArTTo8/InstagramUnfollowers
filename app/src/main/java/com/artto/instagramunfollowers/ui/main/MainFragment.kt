package com.artto.instagramunfollowers.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
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
        }

        sv_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?) = true

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onFilter(newText ?: "")
                return true
            }
        })
    }

    override fun setUsername(username: String) {
        b_toolbar_username.text = username
    }

    override fun notifyDataSetChanged() = recyclerAdapter.notifyDataSetChanged()

    override fun getLayout() = R.layout.fragment_main

}