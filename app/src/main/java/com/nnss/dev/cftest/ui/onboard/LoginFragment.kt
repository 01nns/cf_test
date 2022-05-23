package com.nnss.dev.cftest.ui.onboard

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.Status
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment :
    BaseFragment<FragmentLoginBinding, OnboardViewModel>({
        FragmentLoginBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener
    override val viewModel: OnboardViewModel by viewModel()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
            listener = context
        }
    }

    override fun backPressCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
    }

    override fun initViews() {
        with(ui) {
            btnLogin.setOnClickListener {
                hideKeyboard()
                viewModel.login(
                    LoginRequest(
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                )
            }

            btnReg.setOnClickListener {
                listener.navigateToRegister(findNavController())
            }
        }

    }

    override fun subscribe() {
        with(viewModel) {
            state.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        listener.showLoader(true)
                    }
                    Status.SUCCESS -> {
                        listener.showLoader(false)
                        result.data?.let { data ->
                            saveData(data.token, ui.etUsername.text.toString())
                        }
                        listener.navigateToHome(findNavController())
                    }
                    Status.ERROR -> {
                        listener.showLoader(false)
                        result.msg?.let { listener.showSnackbar(view, it, SnackbarType.ERROR) }
                    }
                }
            }
        }
    }

    interface FragmentListener {
        fun showLoader(show: Boolean)
        fun showSnackbar(view: View?, message: String, type: SnackbarType)
        fun navigateToHome(navController: NavController?)
        fun navigateToRegister(navController: NavController?)
    }

}