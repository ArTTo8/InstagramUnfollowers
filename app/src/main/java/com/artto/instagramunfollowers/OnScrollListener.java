package com.artto.instagramunfollowers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class OnScrollListener extends RecyclerView.OnScrollListener {
    private int previousTotal = 0;
    private int firstVisibleItem;
    private boolean loading = true;

    private LinearLayoutManager mLinearLayoutManager;

    OnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int currentFirstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

//        if (currentFirstVisibleItem > firstVisibleItem)
//            onScrolledUp();
//        else
//            onScrolledDown();
        firstVisibleItem = currentFirstVisibleItem;

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        int visibleThreshold = 5;
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore();
            loading = true;
        }
    }

    public abstract void onLoadMore();
//    public abstract void onScrolledUp();
//    public abstract void onScrolledDown();
}