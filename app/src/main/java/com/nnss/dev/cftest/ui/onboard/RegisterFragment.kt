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
import com.nnss.dev.cftest.databinding.FragmentRegisterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment :
    BaseFragment<FragmentRegisterBinding, OnboardViewModel>({
        FragmentRegisterBinding.inflate(
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
            btnBack.setOnClickListener {
                listener.navigateBack(findNavController())
            }

            btnSubmit.setOnClickListener {
                hideKeyboard()
                viewModel.createUser(
                    CreateUserRequest(
                        etUsername.text.toString(),
                        etPassword.text.toString(),
                        etFirstname.text.toString(),
                        etLastname.text.toString()
                    )
                )
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
                        listener.showSnackbar(view, "User successfully registered!", SnackbarType.SUCCESS)
                        listener.navigateBack(findNavController())
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
        fun navigateBack(navController: NavController?)
    }

}