package com.nnss.dev.cftest.ui.user

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Environment
import com.nnss.dev.cftest.commons.base.BaseViewModel
import com.nnss.dev.cftest.commons.utils.ApiState
import com.nnss.dev.cftest.commons.utils.USERNAME
import com.nnss.dev.cftest.data.remote.model.GenericResponse
import com.nnss.dev.cftest.data.remote.model.UserInfo
import com.nnss.dev.cftest.domain.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class UserViewModel(private val repository: MainRepository, private val prefs: SharedPreferences) :
    BaseViewModel() {

    val userInfoState = MutableStateFlow(ApiState(UserInfo()))
    val updateState = MutableStateFlow(ApiState(GenericResponse()))
    val logoutState = MutableStateFlow(ApiState(GenericResponse()))

    fun getUser() = launch {
        prefs.getString(USERNAME, "")?.let { str ->
            repository.getUser(str)
                .collect {
                    userInfoState.value = it
                }
        }
    }

    fun bitmapToFile(bitmap: Bitmap?, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    fun saveToInternalStorage(context: Context, bitmapImage: Bitmap?, imageName: String): String? {
        val cw = ContextWrapper(context)
// path to /data/data/yourapp/app_data/imageDir
        val directory: File = cw.getDir("profile_image", Context.MODE_PRIVATE)
// Create imageDir
        val mypath = File(directory, imageName)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    fun rotateImage(img: Bitmap, degree: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree)
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }


    fun updateProfile(file: File) = launch {
        val requestBody: RequestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
        val body: MultipartBody = MultipartBody.Builder()
            .addFormDataPart("profileImage", file.name, requestBody)
            .build()

        repository.updateProfile(body)
            .collect {
                updateState.value = it
            }
    }

    fun logout() = launch {
        repository.logout()
            .collect {
                logoutState.value = it
            }
    }

}