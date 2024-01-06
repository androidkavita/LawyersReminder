package com.lawyersbuddy.app.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAddNewClientBinding
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddNewClientActivity : BaseActivity() {
    lateinit var binding: ActivityAddNewClientBinding
    private val AddclientViewModel: ClientViewModel by viewModels()
    var name: String = ""
    var email: String = ""
    var mobile: String = ""
    var address: String = ""
    var client_id: String = ""
    var image: String = ""

    private var userimage: MultipartBody.Part? = null
    private val pickImageCamera = 1
    private val pickImageGallery = 2
    private lateinit var currentPhotoPath: String
    private var mPhotoFile: File? = null
    private var photoURICamera: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_client)

        if (intent != null) {
            if (intent.getStringExtra("type") == "edit") {
                binding.tvTitle.text = "Edit Client"
                binding.addClient.text = "Update"
                name = intent.getStringExtra("name").toString()
                email = intent.getStringExtra("email").toString()
                mobile = intent.getStringExtra("mobile").toString()
                image = intent.getStringExtra("image").toString()
                address = intent.getStringExtra("address").toString()
                client_id = intent.getIntExtra("id", 0).toString()

                binding.emailId.setText(email)
                binding.address.setText(address)
                binding.phoneNumber.setText(mobile)
                binding.clientname.setText(name)


                Glide.with(this).load(image)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                    .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                    .into(binding.ivPict)
            }
        }
        AddclientViewModel.errorString.observe(this) {
            toast(it)
        }
        binding.ivCamera.setOnClickListener {
            selectImage()
        }
        AddclientViewModel.AddClientResponse.observe(this) {
            if (it.status == 1) {
                toast(it.message)
                finish()
            } else {
                toast(it.message)
            }
        }


        AddclientViewModel.progressBarStatus.observe(this) {
                if (it) {
                    showProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }

        AddclientViewModel.editClientResponse.observe(this) {
            if (it.status == 1) {
                toast(it.message)
                finish()
            } else {
                toast(it.message)
            }

        }
        binding.backButton.setOnClickListener {
            finish()
        }


        binding.addClient.setOnClickListener {
            if (userimage == null) {
                val requestFile =
//                        RequestBody.create(MediaType.parse("image/jpg"), "")
                    "".toRequestBody("image/jpg".toMediaTypeOrNull())
                userimage =
                    MultipartBody.Part.createFormData("image", "", requestFile)
            }

            if (binding.clientname.text.trim().isEmpty()) {
                toast("Enter client name.")
            } else if (binding.phoneNumber.text.trim().isEmpty()) {
                toast("Enter mobile number.")
            } else if (binding.phoneNumber.text.length <= 9) {
                toast("Enter valid mobile number.")
            }  else if (binding.address.text.trim().isEmpty()) {
                toast("Enter address.")
            } else {

                if (intent.getStringExtra("type") == "edit") {
                    AddclientViewModel.editClient(
                        "Bearer " + userPref.getToken().toString(),
                        client_id,
                        binding.phoneNumber.text.toString(),
                        binding.address.text.toString(),
                        binding.clientname.text.toString(),
                        binding.emailId.text.toString(),userimage!!
                    )

                } else {
                    AddclientViewModel.add_client(
                        "Bearer " + userPref.getToken().toString(),
                        binding.phoneNumber.text.toString(),
                        binding.address.text.toString(),
                        binding.clientname.text.toString(),
                        binding.emailId.text.toString(),userimage!!
                    )

                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageCamera && resultCode == RESULT_OK) {

            Glide.with(this).load(photoURICamera).into(binding.ivPict)
            val file = File(currentPhotoPath)
            val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
            userimage = MultipartBody.Part.createFormData("image", file.name, requestFile)

        } else if (requestCode == pickImageGallery && data != null) {

            val selectedImage = data.data
            try {

                Glide.with(this).load(selectedImage).into(binding.ivPict)
                val file = File(getPath(selectedImage!!))
                val requestFile = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
                userimage = MultipartBody.Part.createFormData("image", file.name, requestFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else{

        }
    }


    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val pm: PackageManager = packageManager
        val builder =
            android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
        builder.setTitle("Select Option")
        builder.setItems(
            options
        ) { dialog: DialogInterface, item: Int ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                requestStoragePermission(true)

            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                requestStoragePermission(false)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            takePictureIntent.resolveActivity(this.packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri =
                        FileProvider.getUriForFile(
                            this,
                            "com.lawyersbuddy.app.myUniquefileprovider",
                            photoFile
                        )
                    mPhotoFile = photoFile
                    photoURICamera = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, pickImageCamera)
                }
            }
        }

    }

    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            android.content.CursorLoader(this, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
    private fun requestStoragePermission(isCamera: Boolean) {
        Dexter.withActivity(this)
            .withPermissions(

                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        if (isCamera) {
                            openCamera()
                        } else {
                            openGallery()
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .withErrorListener { error ->

            }
            .onSameThread()
            .check()
    }
    private fun openGallery() {
        val pickPhoto =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, pickImageGallery)
    }
    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS"
        ) { dialog: DialogInterface, which: Int ->
            openSettings()
            dialog.cancel()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        builder.show()
    }
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", "com.lawyersbuddy.app", null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
}