package com.artto.unfollowers.ui.rate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.unfollowers.R
import com.artto.unfollowers.ui.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_rate.*
import org.koin.android.ext.android.inject

class RateDialogFragment : BaseDialogFragment(), RateView {

    @InjectPresenter
    lateinit var presenter: RatePresenter

    @ProvidePresenter
    fun providePresenter() = inject<RatePresenter>().value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_rate_later.setOnClickListener { dismiss() }
        b_rate.setOnClickListener { presenter.onRateClicked() }
    }

    override fun openGooglePlay() {
        kotlin.runCatching { startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=com.artto.unfollowers".toUri())) }
        dismiss()
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            setLayout(
                    (resources.displayMetrics.widthPixels * 0.9).toInt(),
                    attributes.height)

            setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun getLayout() = R.layout.dialog_rate
}