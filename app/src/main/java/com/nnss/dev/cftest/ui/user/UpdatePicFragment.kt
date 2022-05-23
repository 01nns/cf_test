package com.nnss.dev.cftest.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nnss.dev.cftest.commons.base.BaseFragment
import com.nnss.dev.cftest.commons.utils.SnackbarType
import com.nnss.dev.cftest.commons.utils.Status
import com.nnss.dev.cftest.commons.utils.collectLA
import com.nnss.dev.cftest.databinding.FragmentUpdatePicBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class UpdatePicFragment :
    BaseFragment<FragmentUpdatePicBinding, UserViewModel>({
        FragmentUpdatePicBinding.inflate(
            it
        )
    }) {

    private lateinit var listener: FragmentListener

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

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

            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        btnSave.isEnabled = true
                        handleCameraImage(result.data)
                    } else {
                        btnSave.isEnabled = false
                    }
                }

            ui.btnCapture.setOnClickListener {
                if (listener.hasCameraPermission()) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    resultLauncher.launch(cameraIntent)
                }
            }

            btnBack.setOnClickListener {
                listener.navigateBack(findNavController())
            }

            btnSave.setOnClickListener {
                val path: File? = activity?.getDir("profile_image", Context.MODE_PRIVATE)
                val file = File(path, "profile.png")
                viewModel.updateProfile(file)
            }

        }

    }

    @SuppressLint("SetTextI18n")
    override fun subscribe() {
        with(viewModel) {
            updateState.collectLA(viewLifecycleOwner) { result ->
                when (result.status) {
                    Status.IDLE -> {}
                    Status.LOADING -> {
                        listener.showLoader(true)
                    }
                    Status.SUCCESS -> {
                        listener.showLoader(false)
                        listener.showSnackbar(
                            view,
                            "Profile Pic Successfully Updated!",
                            SnackbarType.SUCCESS
                        )
                        listener.navigateBack(findNavController())
                    }
                    Status.ERROR -> {
                        listener.showLoader(false)
                        Timber.e(result.msg)
                        result.msg?.let { listener.showSnackbar(view, it, SnackbarType.ERROR) }
                    }
                }
            }
        }
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        val newBitmap = viewModel.rotateImage(bitmap, 90F)
        ui.imgProfilePic.setImageBitmap(newBitmap)
        viewModel.saveToInternalStorage(requireActivity(), newBitmap, "profile.png")

    }

    interface FragmentListener {
        fun showLoader(show: Boolean)
        fun showSnackbar(view: View?, message: String, type: SnackbarType)
        fun navigateBack(navController: NavController?)
        fun hasCameraPermission() : Boolean
    }

}