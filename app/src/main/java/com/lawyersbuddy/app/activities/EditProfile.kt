package com.lawyersbuddy.app.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lawyersbuddy.app.DialogUtils


import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.fragments.clicklistener.popupItemClickListnerCountry
import com.lawyersbuddy.app.databinding.ActivityEditProfileBinding
import com.lawyersbuddy.app.model.CategoryModel
import com.lawyersbuddy.app.model.CityListData
import com.lawyersbuddy.app.model.StateListData
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.GetProfileViewModel
import com.lawyersbuddy.app.viewmodel.StateListViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfile : BaseActivity() , popupItemClickListnerCountry {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: GetProfileViewModel by viewModels()
    private val stateViewModel: StateListViewModel by viewModels()
    lateinit var statelist: ArrayList<StateListData>
    lateinit var citylist: ArrayList<CityListData>
    var cityList = ArrayList<String>()
    var stateList = ArrayList<String>()
    var flag: String = ""
    lateinit var mContext: Context

    var state_id: ArrayList<String> = ArrayList()
    private var cityListId = ArrayList<Int>()
    private var stateListId = ArrayList<Int>()
         private lateinit var recyclerView: RecyclerView
    private lateinit var search: EditText
    lateinit var adapterState: StatePopup
    lateinit var adapterCity: CityListPopUp

    private var userimage: MultipartBody.Part? = null
    private val pickImageCamera = 1
    private val pickImageGallery = 2
    private lateinit var currentPhotoPath: String
    private var mPhotoFile: File? = null
    private var photoURICamera: Uri? = null
    var countryName = ""
    var stateName = ""
    var stateId = 0
    var countryId = 0
    lateinit var progressbar: ProgressBar
    lateinit var no_notification: LinearLayout
    lateinit var progressbarpopup: ProgressBar
    var StateListModel = ArrayList<CategoryModel>()
    var subcategoryModel = ArrayList<CategoryModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        statelist = ArrayList()
        citylist = ArrayList()
        mContext=this.applicationContext

        if (userPref.getUserProfileImage().equals("")) {

        } else {
            Glide.with(this).load(userPref.getUserProfileImage())
                .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                .into(binding.ivPict)
        }

        binding.arrow.setOnClickListener {
            finish()
        }

        binding.ivCamera.setOnClickListener {
            selectImage()
        }

        viewModel.errorString.observe(this) {
            toast(it)
        }

        stateViewModel.StateListApi()




        viewModel.profileViewResponse.observe(this) {
            if (it.status == 1) {


                Glide.with(this).load(it.data?.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.user_image_place_holder))
                    .apply(RequestOptions.errorOf(R.drawable.user_image_place_holder))
                    .into(binding.ivPict)

//                for (i in 0 until state.size) {
//                    if (state.get(i) == it.data?.state) {
//                        binding.spinnerStateList.setSelection(i)
//                        break
//                    }
//                }
//                for (i in 0 until city.size) {
//                    if (city.get(i) == it.data?.city) {
//                        binding.spinnerCityList.setSelection(i)
//                        break
//                    }
//                }

                binding.etFirmName.setText(it.data!!.firmName.toString())
                binding.etAddress.setText(it.data!!.address.toString())
                binding.etBarAssociation.setText(it.data?.barAssociation.toString())
                binding.etBarCouncilNumber.setText(it.data?.barCouncilNumber.toString())
                binding.etName.setText(it.data?.name.toString())
                binding.tvMobile.text = it.data?.mobile.toString()
                binding.tvEmail.text = it.data?.email.toString()
                binding.tvCity.text = it.data?.city.toString()
                binding.tvState.text = it.data?.state.toString()



                binding.etZipCode.setText(it.data?.zipCode.toString())




            } else {
                Log.d("Response", it.toString())
                toast(this, it.message!!)
            }
        }
        binding.btnUpdate.setOnClickListener {

            if (userimage == null) {
                val requestFile =
//                        RequestBody.create(MediaType.parse("image/jpg"), "")
                    "".toRequestBody("image/jpg".toMediaTypeOrNull())
                userimage =
                    MultipartBody.Part.createFormData("image", "", requestFile)
            }

            if (validate()) {
                viewModel.editProfileApi(
                    "Bearer " + userPref.getToken().toString(),
                    binding.etName.text.trim().toString(),
                    binding.tvState.text.toString(),
                    binding.tvCity.text.toString(),
                    binding.etBarAssociation.text.trim().toString(),
                    binding.etBarCouncilNumber.text.trim().toString(),
                    binding.etFirmName.text.toString(),
                    binding.etZipCode.text.toString(),
                    binding.etAddress.text.trim().toString(),
                    userimage!!
                )
            }

        }


        viewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        viewModel.editProfileViewResponse.observe(this) {
            if (it.status == 1) {

                userPref.isLogin = true
                userPref.user = it.data

                userPref.setName(it.data.name)
                userPref.setEmail(it.data.email)
                userPref.setMobile(it.data.mobile)
                userPref.setAddress(it.data.address)

                userPref.setid(it.data.id.toString())
                userPref.setProfileImage(it.data.image)
                Log.e("@@image", it.data.image)
                userPref.setStateId(it.data.state_id.toString())
//                userPref.setCityId(it.data.city_id.toString())

                toast("Profile Updated Successfully...")
                finish()
            } else {
                hideProgressDialog()
                toast(it.message)
            }
        }

        viewModel.get_profileApi("Bearer " + userPref.getToken().toString())


        binding.tvState.setOnClickListener {

                flag = "State"
             openPopUp()

        }
        binding.tvCity.setOnClickListener {
            if ( userPref.getStateId().equals("0")) {
                toast("Please select state first.")

            } else {
                flag = "City"
                openPopUp()
            }
        }

    }

    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp() {

        try {
            val binding = LayoutInflater.from(this).inflate(R.layout.pop_lists, null)
            dialog = DialogUtils().createDialog(this, binding.rootView, 0)!!
            recyclerView = binding.findViewById(R.id.popup_recyclerView)

            search = binding.findViewById(R.id.search_bar_edittext_popuplist)
            no_notification = binding.findViewById(R.id.no_notification)
            recyclerView.layoutManager = LinearLayoutManager(this)
//            progressbarpopup = binding.findViewById(R.id.progressbar_pop)
            val dialougTitle = binding.findViewById<TextView>(R.id.popupTitle)
            val dialougbackButton = binding.findViewById<ImageView>(R.id.BackButton)
            val SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            SearchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(text: Editable?) {
                        filterData(text.toString(), flag)
                }

            })
            dialougbackButton.setOnClickListener { dialog!!.dismiss() }


//            var SearchEditText = binding.findViewById<EditText>(R.id.search_bar_edittext_popuplist)

            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {

//                    CN_SEARCH(s.toString())

                }
            })


          if (flag.equals("State")) {

                dialougTitle.setText(flag)
                stateViewModel.StateListApi()
          }else if (flag.equals("City")) {

                dialougTitle.setText(flag)
                stateViewModel.city_list(userPref.getStateId().toString())
            }
            dialog!!.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        stateViewModel.StateListResponseModel.observe(this) {
            if (it?.status == 1) {
                statelist = it.data
                setStateAdapter()

            } else {
                //toast(it.message)
                snackbar(it?.message!!)
            }
        }
        stateViewModel.CityListResponseModel.observe(this) {
            if (it?.status == 1) {
                   citylist=it.data
                    setCityAdapter()


            }else {
                snackbar(it?.message!!)
            }
        }

    }
    private fun filterData(searchText: String, flag: String) {
        var filteredStateList: ArrayList<StateListData> = ArrayList()
        var filteredCityList: ArrayList<CityListData> = ArrayList()

        if (flag.equals("State")) {
            if (statelist != null) {
                for (item in statelist) {
                    try {
                        if (item.state.toLowerCase().contains(searchText.toLowerCase())) {
                            filteredStateList.add(item)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }  else if (flag.equals("City")) {
            if (citylist != null) {
                for (item in citylist) {
                    try {
                        if (item.city.toLowerCase().contains(searchText.toLowerCase())) {
                            filteredCityList.add(item)
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        try {
             if (flag.equals("State")) {
                adapterState.filterList(filteredStateList)
            } else if (flag.equals("City")) {
                adapterCity.filterList(filteredCityList)
            }

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    fun setStateAdapter() {
        adapterState = this.let { StatePopup(mContext, statelist, flag, this) }!!
        recyclerView.adapter = adapterState
    }
    fun setCityAdapter() {
        adapterCity = this.let { CityListPopUp(mContext, citylist, flag, this) }!!
        recyclerView.adapter = adapterCity
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


    fun validate(): Boolean {

        if (binding.etName.text.trim().isEmpty()) {
            binding.etName.error = "Please enter your name"
            binding.etName.requestFocus()
            return false
        }
        else if (binding.etBarAssociation.text.trim().isEmpty()) {
            binding.etBarAssociation.error = "Please enter Bar Association"
            binding.etBarAssociation.requestFocus()
            return false
        }
        else if (binding.etBarCouncilNumber.text.trim().isEmpty()) {
            binding.etBarCouncilNumber.error = "Please enter council Number"
            binding.etBarCouncilNumber.requestFocus()
            return false
        }


        return true
    }

    override fun getCountry(name: String, flag: String, id: Int) {
       if (flag.equals("State")) {
            userPref.setStateId(id.toString())
            binding.tvState.text = name
            binding.tvCity.text="Select"
            dialog?.dismiss()
        } else if (flag.equals("City")) {
            binding.tvCity.text = name
            dialog?.dismiss()
        }
    }

}