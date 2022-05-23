package com.nnss.dev.cftest.ui.post

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.Status
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.databinding.FragmentNewPostBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPostFragment :
    BaseFragment<FragmentNewPostBinding, PostViewModel>({
        FragmentNewPostBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener

    override val viewModel: PostViewModel by viewModel()
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
            btnSubmit.setOnClickListener {
                hideKeyboard()
                viewModel.newPost(NewPostRequest(etPost.text.toString()))
            }

            btnBack.setOnClickListener {
                listener.navigateBack(findNavController())
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
                        listener.navigateBack(findNavController())
                        listener.showSnackbar(view, "New Post Successfully Created!", SnackbarType.SUCCESS)
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
    }

}