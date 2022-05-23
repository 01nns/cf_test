package com.nnss.dev.cftest.ui.home

import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.Status
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.data.remote.model.FeedResponse
import com.nnss.dev.cftest.databinding.FragmentHomeBinding
import com.nnss.dev.cftest.ui.adapter.PostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>({
        FragmentHomeBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener
    private lateinit var mAdapter: PostAdapter

    override val viewModel: HomeViewModel by viewModel()
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
            mAdapter = PostAdapter(requireActivity(), object : PostAdapter.IClickListener {
                override fun onItemClick(item: FeedResponse) {
                    listener.navigateToViewPost(findNavController(), item)
                }

            })

            rvPosts.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
            viewModel.getFeed()

            btnNewPost.setOnClickListener {
                listener.navigateToNewPost(findNavController())
            }

            btnUser.setOnClickListener {
                listener.navigatetoUser(findNavController())
            }

        }

    }

    override fun subscribe() {
        with(viewModel) {
            state.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        shimmer(true)
                    }
                    Status.SUCCESS -> {
                        shimmer(false)
                        result.data?.let { data ->
                            val sorted = data.sortedByDescending { it.id }
                            mAdapter.setItems(sorted)
                        }
                    }
                    Status.ERROR -> {
                        Timber.e(result.msg)
                        result.msg?.let { listener.showSnackbar(view, it, SnackbarType.ERROR) }
                    }
                }
            }
        }
    }

    private fun shimmer(show: Boolean) {
        if (show) {
            ui.viewShimmer.startShimmer()
            ui.viewShimmer.visibility = View.VISIBLE
            ui.rvPosts.visibility = View.GONE
        } else {
            ui.viewShimmer.stopShimmer()
            ui.viewShimmer.visibility = View.GONE
            ui.rvPosts.visibility = View.VISIBLE
        }
    }

    interface FragmentListener {
        fun showLoader(show: Boolean)
        fun showSnackbar(view: View?, message: String, type: SnackbarType)
        fun navigateToNewPost(navController: NavController?)
        fun navigatetoUser(navController: NavController?)
        fun navigateToViewPost(navController: NavController?, item: FeedResponse)
    }

}