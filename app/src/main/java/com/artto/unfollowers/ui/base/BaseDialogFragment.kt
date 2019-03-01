package com.artto.unfollowers.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artto.unfollowers.utils.moxy.MvpAppCompatDialogFragment

abstract class BaseDialogFragment : MvpAppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getLayout(), container, false)

    abstract fun getLayout(): Int

}