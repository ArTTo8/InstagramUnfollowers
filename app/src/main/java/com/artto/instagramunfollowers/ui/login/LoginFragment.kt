package com.artto.instagramunfollowers.ui.login

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.artto.instagramunfollowers.R
import com.artto.instagramunfollowers.ui.base.BaseFragment
import com.artto.instagramunfollowers.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.item_user.*
import org.koin.android.ext.android.inject

class LoginFragment : BaseFragment(), LoginView {

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter() = inject<LoginPresenter>().value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        b_login.setOnClickListener {
            presenter.onLoginClicked(
                    et_username.text.toString(),
                    et_password.text.toString())

            view.hideKeyboard()
        }
    }

    override fun navigateToMain() =
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())

    override fun showInputs() {
        et_username.visibility = View.VISIBLE
        et_password.visibility = View.VISIBLE
        b_login.visibility = View.VISIBLE
    }

    override fun getLayout() = R.layout.fragment_login

}