package com.artto.instagramunfollowers.ui.login

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.instagramunfollowers.R
import com.artto.instagramunfollowers.ui.base.BaseFragment
import com.artto.instagramunfollowers.utils.extension.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(), LoginView {

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter() = inject<LoginPresenter>().value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_login.setOnClickListener {
            if (et_username.checkNotBlank() && et_password.checkNotBlank()) {
                presenter.onLoginClicked(
                        et_username.text.toString(),
                        et_password.text.toString())

                view.hideKeyboard()
            }
        }
    }

    private fun EditText.checkNotBlank(): Boolean = if (text.isBlank()) {
        (background as? GradientDrawable)?.setStroke(1, Color.RED)
        false
    } else {
        (background as? GradientDrawable)?.setStroke(1, ContextCompat.getColor(context, R.color.colorAccent))
        true
    }

    override fun navigateToMain() = findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())

    override fun showInputs(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        et_username.visibility = visibility
        et_password.visibility = visibility
        b_login.visibility = visibility
    }

    override fun onLoginFailed() {
        (b_login.background as? GradientDrawable)?.setStroke(1, Color.RED)
    }

    override fun getLayout() = R.layout.fragment_login

}