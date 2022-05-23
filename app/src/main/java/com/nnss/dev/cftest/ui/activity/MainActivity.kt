package com.nnss.dev.cftest.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.nnss.dev.cftest.R
import com.nnss.dev.cftest.commons.base.BaseActivity
import com.nnss.dev.cftest.commons.utils.POST_DATA
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.UIDialog
import com.nnss.dev.cftest.data.remote.model.FeedResponse
import com.nnss.dev.cftest.databinding.ActivityMainBinding
import com.nnss.dev.cftest.ui.home.HomeFragment
import com.nnss.dev.cftest.ui.home.HomeFragmentDirections
import com.nnss.dev.cftest.ui.onboard.LoginFragment
import com.nnss.dev.cftest.ui.onboard.LoginFragmentDirections
import com.nnss.dev.cftest.ui.onboard.RegisterFragment
import com.nnss.dev.cftest.ui.post.NewPostFragment
import com.nnss.dev.cftest.ui.post.ViewPostFragment
import com.nnss.dev.cftest.ui.user.UpdatePicFragment
import com.nnss.dev.cftest.ui.user.UserFragment
import com.nnss.dev.cftest.ui.user.UserFragmentDirections

class MainActivity : BaseActivity<ActivityMainBinding>({ ActivityMainBinding.inflate(it)}),
    LoginFragment.FragmentListener,
    RegisterFragment.FragmentListener,
    HomeFragment.FragmentListener,
    NewPostFragment.FragmentListener,
    UserFragment.FragmentListener,
    UpdatePicFragment.FragmentListener,
    ViewPostFragment.FragmentListener{

    private val REQUEST_CODE = 201
    private var hasCameraPermission = false

    override fun initViews() {
        loader = UIDialog.loader(this)
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navView.setupWithNavController(navController)

        binding.appBarMain.btnBurger.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // permission granted
            hasCameraPermission = true
        } else {
            // permission not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            hasCameraPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun showLoader(show: Boolean) {
        if (show) {
            loader?.show()
        } else {
            loader?.dismiss()
        }
    }

    override fun showSnackbar(view: View?, message: String, type: SnackbarType) {
        val snack = view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT) }
        val color = if (type == SnackbarType.ERROR) R.color.black else R.color.primary
        snack?.view?.setBackgroundColor(ContextCompat.getColor(this, color))
        snack?.show()
    }

    override fun navigateToNewPost(navController: NavController?) {
       navController?.navigate(HomeFragmentDirections.actionHomeFragmentToNewPostFragment())
    }

    override fun navigatetoUser(navController: NavController?) {
        navController?.navigate(HomeFragmentDirections.actionHomeFragmentToUserFragment())
    }

    override fun navigateToViewPost(navController: NavController?, item: FeedResponse) {
        val bundle = Bundle()
        bundle.putParcelable(POST_DATA, item)
        navController?.navigate(R.id.viewPostFragment, bundle)
    }

    override fun navigateToHome(navController: NavController?) {
        navController?.navigate(LoginFragmentDirections.actionNavLoginToHomeFragment2())
    }

    override fun navigateToRegister(navController: NavController?) {
        navController?.navigate(LoginFragmentDirections.actionNavLoginToRegisterFragment())
    }

    override fun navigateBack(navController: NavController?) {
        navController?.navigateUp()
    }

    override fun hasCameraPermission(): Boolean {
        return hasCameraPermission
    }

    override fun navigateToLogin(navController: NavController?) {
        navController?.navigate(UserFragmentDirections.actionUserFragmentToNavLogin())
    }

    override fun navigateToUpdatePic(navController: NavController?) {
        navController?.navigate(UserFragmentDirections.actionUserFragmentToUpdatePicFragment())
    }
}