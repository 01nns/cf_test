package com.nnss.dev.cftest.ui.post

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.*
import com.nnss.dev.cftest.data.remote.model.Comments
import com.nnss.dev.cftest.data.remote.model.FeedResponse
import com.nnss.dev.cftest.databinding.FragmentViewPostBinding
import com.nnss.dev.cftest.ui.adapter.CommentsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewPostFragment :
    BaseFragment<FragmentViewPostBinding, PostViewModel>({
        FragmentViewPostBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener
    private var postData: FeedResponse? = null
    private var comments: List<Comments>? = null
    private lateinit var mAdapter: CommentsAdapter

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

    @SuppressLint("SetTextI18n")
    override fun initViews() {
        with(ui) {
            postData = arguments?.getParcelable(POST_DATA)
            comments = postData?.comments

            Glide.with(requireActivity()).load(postData?.user?.profilePic).into(imgProfilePic)
            textName.text = postData?.user?.firstName + postData?.user?.lastName
            textPost.text = postData?.text
            textUsername.text = postData?.user?.username
            textDateCreated.text = postData?.createdAt

            btnBack.setOnClickListener {
                listener.navigateBack(findNavController())
            }

            mAdapter = CommentsAdapter()

            rvComments.apply {
                layoutManager = LinearLayoutManager(requireActivity())
                adapter = mAdapter
            }

            if (comments != null) {
                rvComments.visibility = View.VISIBLE
                empty.visibility = View.GONE
                textAllComments.visibility = View.VISIBLE
                mAdapter.setItems(comments?.sortedByDescending { it.id })
            } else {
                rvComments.visibility = View.GONE
                empty.visibility = View.VISIBLE
                textAllComments.visibility = View.GONE

            }

            btnAddComment.setOnClickListener {
                viewModel.newComment(postData?.id, NewCommentRequest(etComment.text.toString()))
            }
        }

    }

    override fun subscribe() {
        with(viewModel) {
            commentState.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        listener.showLoader(true)
                    }
                    Status.SUCCESS -> {
                        listener.showLoader(false)
                        listener.navigateBack(findNavController())
                        listener.showSnackbar(view, "New Comment Successfully Created!", SnackbarType.SUCCESS)
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