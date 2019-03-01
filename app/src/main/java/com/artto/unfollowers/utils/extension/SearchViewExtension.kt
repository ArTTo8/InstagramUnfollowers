package com.artto.unfollowers.utils.extension

import androidx.appcompat.widget.SearchView

fun SearchView.setOnQueryTextChangedListener(listener: (String) -> Boolean) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(query: String?) = true

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let { return listener.invoke(it) } ?: return false
        }

    })
}