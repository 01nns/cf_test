package com.nnss.dev.cftest.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nnss.dev.cftest.R
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.Status
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.databinding.FragmentUserBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserFragment :
    BaseFragment<FragmentUserBinding, UserViewModel>({
        FragmentUserBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener

    override val viewModel: UserViewModel by viewModel()
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
            viewModel.getUser()
            btnBack.setOnClickListener {
                listener.navigateBack(findNavController())
            }

            btnEdit.setOnClickListener {
                listener.navigateToUpdatePic(findNavController())
            }

            btnLogout.setOnClickListener {
                viewModel.logout()
            }

        }

    }

    @SuppressLint("SetTextI18n")
    override fun subscribe() {
        with(viewModel) {
            userInfoState.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        listener.showLoader(true)
                    }
                    Status.SUCCESS -> {
                        result.data?.let { data ->
                            Glide.with(requireActivity())
                                .load(data.profilePic)
                                .placeholder(R.drawable.ic_user)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        listener.showLoader(false)
                                        listener.showSnackbar(view, "Error Loading Image!", SnackbarType.ERROR)
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable?,
                                        model: Any?,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource?,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        listener.showLoader(false)
                                        return false
                                    }

                                })
                                .into(ui.imgProfilePic)

                            ui.textName.text = data.firstName + " " + data.lastName
                            ui.textUserId.text = data.username
                        }

                    }
                    Status.ERROR -> {
                        listener.showLoader(false)
                        Timber.e(result.msg)
                        result.msg?.let { listener.showSnackbar(view, it, SnackbarType.ERROR) }
                    }
                }
            }

            logoutState.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        listener.showLoader(true)
                    }
                    Status.SUCCESS -> {
                        listener.showLoader(false)
                        listener.navigateToLogin(findNavController())
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
        fun navigateBack(navController: NavController?)
        fun navigateToLogin(navController: NavController?)
        fun navigateToUpdatePic(navController: NavController?)
    }

}