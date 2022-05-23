package com.nnss.dev.cftest.commons.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding, M : BaseViewModel>(val bindingFactory: (LayoutInflater) -> B) :
    Fragment() {
    lateinit var ui: B
    protected abstract val viewModel: M

    abstract fun backPressCallback(): OnBackPressedCallback
    abstract fun initViews()
    abstract fun subscribe()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun init() {
        ui = bindingFactory(layoutInflater)
        activity?.let {
            requireActivity().onBackPressedDispatcher.addCallback(it, backPressCallback())
        }
    }
}