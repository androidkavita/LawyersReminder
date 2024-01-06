package com.lawyersbuddy.app.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.lawyersbuddy.app.CommonMethod.CommonMethods
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.adapter.HearingImagesAdapter
import com.lawyersbuddy.app.adapter.ImageAdapter2
import com.lawyersbuddy.app.baseClasses.BaseActivity
import com.lawyersbuddy.app.databinding.ActivityAddNewHearingBinding
import com.lawyersbuddy.app.fragments.clicklistener.*
import com.lawyersbuddy.app.model.Images
import com.lawyersbuddy.app.model.ImagesModel
import com.lawyersbuddy.app.model.file_detail
import com.lawyersbuddy.app.util.CommonUtils
import com.lawyersbuddy.app.util.DateFormat
import com.lawyersbuddy.app.util.ImageRotation
import com.lawyersbuddy.app.util.toast
import com.lawyersbuddy.app.viewmodel.HearingViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class AddNewHearingActivity : BaseActivity(), Clickcase, imageclick, ClickIMage, clickpdf,
    DeleteImage {
    lateinit var binding: ActivityAddNewHearingBinding
    private val hearingViewModel: HearingViewModel by viewModels()

    var first_date = ""
    var second_date = ""
    var Month_DAy = 0
    var Month_year = 0
    var type1 = ""
    val FILE_BROWSER_CACHE_DIR = "hearing_doc[]"
    private lateinit var hearingAdapetr: HearingImagesAdapter
    private var addimage: ArrayList<String> = ArrayList()
    var imagePath = ""
    var imageArray: ArrayList<String> = ArrayList()
    var requestMultiImagesAndVideos = ArrayList<MultipartBody.Part?>()
    var imageFile: File? = null
    lateinit var photoURI: Uri
    private var images: ArrayList<Images> = ArrayList()
    private var list: ArrayList<file_detail> = ArrayList()
    private var imagesmodel: ArrayList<ImagesModel> = ArrayList()
    val part: ArrayList<MultipartBody.Part?> = java.util.ArrayList()
    var timeMillies: Long = 123456789
    var timeInMilliseconds: Long = 123456789
    var Year_ = 0
    var datePicker: DatePickerDialog? = null

    var currentdate = ""
    var nexthearingdate = ""
    val SELECT_PICTURES = 200
    var image = ""
    var type = ArrayList<MultipartBody.Part?>()
    var userimage = ArrayList<MultipartBody.Part?>()
    private val pickImageCamera = 1

    var currentPhotoPath = ""

    var hearing_id = ""

    var Date = ""
    lateinit var adapter1: ImageAdapter2
    var finalhearing = ""
    var reminder = ""
    lateinit var adapter: HearingImagesAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_hearing)

        if (intent != null) {
            hearing_id = intent.getStringExtra("hearing_id").toString()
        }
        hearingViewModel.hearing_detailApi("Bearer " + userPref.getToken().toString(), hearing_id)
        hearingViewModel.progressBarStatus.observe(this) {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }
        hearingViewModel.deleteimageresponse.observe(this) {
            if (it?.status == 1) {
                hearingViewModel.hearing_detailApi(
                    "Bearer " + userPref.getToken().toString(),
                    hearing_id
                )

            } else {
                toast(it.message)
            }
        }

        hearingViewModel.hearingDetailsResponse.observe(this) {
            if (it?.status == 1) {
                try {
                    binding.finalhearing.isChecked = it.data?.final_date == 1
                    binding.reminder.isChecked = it.data?.is_auto_reminder == 1
                    binding.etHearingDetails.setText(
                        it.data!!.hearingDetails.toString()
                    )
                    binding.etPartyName.text = it.data!!.clientName
                    binding.etCaseStage.setText(it.data!!.caseStage)
                    binding.etCaseStatus.setText(it.data!!.caseStatus)
                    binding.etCaseTitle.text = it.data!!.caseTitle
                    currentdate = CommonUtils.getDate_format(it.data!!.currentDate.toString(), 0)
                    binding.tvCurrentDate.text = DateFormat.date(currentdate)
                    first_date = currentdate
                    nexthearingdate =
                        CommonUtils.getDate_format(it.data!!.hearingDate.toString(), 0)
                    binding.tvNextHearingdate.text = DateFormat.date(nexthearingdate)
                    list.clear()
                    list.addAll(it.data?.file_detail!!)
//                    Glide.with(this).load(it.data?.hearingDoc).placeholder(R.drawable.ic_baseline_picture_as_pdf_24).into(binding!!.ivPict)
                    binding.rvPrevious.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    adapter1 = ImageAdapter2(this, list, this, this)
                    binding.rvPrevious.adapter = adapter1

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else {
                snackbar(it?.message!!)
            }
        }
        hearingViewModel.addHearingResponse.observe(this) {
            if (it.status == 1) {
                toast(it.message.toString())
                finish()
            } else {
                toast(it.message.toString())
            }

        }
        binding.arrow.setOnClickListener {
            finish()
        }
        hearingViewModel.errorString.observe(this) {
            toast(it)
        }
        setAdapter()
        binding.btnAddHearing.setOnClickListener {

            if (binding.finalhearing.isChecked) {
                finalhearing = "1"
            } else {
                finalhearing = "0"
            }
            if (binding.reminder.isChecked) {
                reminder = "1"
            } else {
                reminder = "0"
            }
            if (image.equals("pdf")) {
                type = userimage
            } else {
                type = part
            }
            if (validate()) {
                hearingViewModel.EditHearingApi(
                    "Bearer " + userPref.getToken().toString(),
                    hearing_id, binding.etCaseStage.text.toString(),
                    binding.etCaseStatus.text.toString(),
                    currentdate,
                    binding.etHearingDetails.text.toString(),
                    nexthearingdate,
                    finalhearing, reminder, type1, requestMultiImagesAndVideos
                )
            }
        }

        hearingViewModel.progressBarStatus.observe(this, androidx.lifecycle.Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        hearingViewModel.EditHearingResponse.observe(this, androidx.lifecycle.Observer {
            if (it.status == 1) {
                toast("Hearing Updated Successfully...")
                finish()
            } else {
                hideProgressDialog()
                toast(it.message.toString())
            }
        })

        binding.llCam.setOnClickListener {
            selectImage()
        }

        binding.llCurrentDate.setOnClickListener {
            clickHearingDatePicker()
            Date = "current"
        }

        binding.llHearingDate.setOnClickListener {
            NextHearingDatePicker()
            Date = "hearing"
        }

//        setAdapter()
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun clickHearingDatePicker() {

        val myCalendar = Calendar.getInstance()
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        first_date = day.toString() + "-" + month + 1 + "-" + year

        datePicker = DatePickerDialog(this)
        datePicker = DatePickerDialog(
            this, R.style.DatePickerTheme,
            { view, year, month, dayOfMonth -> // adding the selected date in the edittext
                Month_DAy = dayOfMonth
                Month_year = (month + 1)
                Year_ = year
                timeMillies = myCalendar.timeInMillis
                first_date = Month_DAy.toString() + "-" + Month_year + "-" + Year_
                if (Date.equals("current")) {
                    currentdate = dayOfMonth.toString() + "-" + (month + 1) + "-" + year
                    binding.tvCurrentDate.text = DateFormat.date(currentdate)
                } else {
                }

            }, year, month, day
        )

        val inFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inFormat.parse(first_date)
        val outFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
        val goal: String = outFormat.format(date)
        val date_test = goal
        val formatter: DateTimeFormatter =
            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
        val localDate: LocalDateTime = LocalDateTime.parse(date_test, formatter)
        timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
        Log.d("TAG", "Date in milli :: FOR API >= 26 >>> $timeInMilliseconds")
        datePicker!!.datePicker.minDate = timeInMilliseconds
        datePicker!!.show()

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun NextHearingDatePicker() {
        Log.d(
            "TAG",
            "NextHearingDatePicker: " + Month_DAy + " month " + Month_year + " year " + Year_
        )
        Log.d("TAG", "NextHearingText: next date " + first_date)
        try {
                datePicker = DatePickerDialog(this)
                datePicker = DatePickerDialog(
                this, R.style.DatePickerTheme,
                { view, Year_, Month_year, Month_DAy -> // adding the selected date in the edittext
                    second_date =
                        Month_DAy.toString() + "-" + (Month_year + 1).toInt() + "-" + Year_

                    nexthearingdate = Month_DAy.toString() + "-" + (Month_year + 1) + "-" + Year_
                    binding.tvNextHearingdate.text = DateFormat.date(nexthearingdate)

                }, Year_, Month_year - 1, Month_DAy
            )
            val inFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: Date = inFormat.parse(first_date)
            val outFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
            val goal: String = outFormat.format(date)
            val date_test = goal
            val formatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
            val localDate: LocalDateTime = LocalDateTime.parse(date_test, formatter)
            timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()

            Log.d("TAG", "NextHearingDatePicker: " + first_date)
            Log.d("TAG", "Date in milli 0000000:: FOR API >= 26 >>> $timeInMilliseconds")

            datePicker!!.datePicker.minDate = timeInMilliseconds
            datePicker!!.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    @SuppressLint("Range")
    @Nullable
    private fun getFileDisplayName(uri: Uri): String? {
        var displayName: String? = null
        contentResolver
            .query(uri, null, null, null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    displayName =
                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        return displayName

    }

    @Throws(IOException::class)
    private fun writeFileContent(uri: Uri): String? {
        val selectedFileInputStream = contentResolver.openInputStream(uri)
        if (selectedFileInputStream != null) {

            val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }

            val certCacheDir = File(mediaDir, FILE_BROWSER_CACHE_DIR)
            var isCertCacheDirExists = certCacheDir.exists()
            if (!isCertCacheDirExists) {
                isCertCacheDirExists = certCacheDir.mkdirs()
            }
            if (isCertCacheDirExists) {
                val fileName: String? = getFileDisplayName(uri)
                fileName?.replace("[^a-zA-Z0-9]", " ")
                val filePath = certCacheDir.absolutePath.toString() + "/" + fileName
                val selectedFileOutPutStream: OutputStream = FileOutputStream(filePath)
                val buffer = ByteArray(1024)
                var length: Int
                while (selectedFileInputStream.read(buffer).also { length = it } > 0) {
                    selectedFileOutPutStream.write(buffer, 0, length)
                }
                selectedFileOutPutStream.flush()
                selectedFileOutPutStream.close()
                return filePath
            }
            selectedFileInputStream.close()
        }
        return null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && data != null) {
            val fileUris = data.data
            image = "pdf"
            var path = writeFileContent(fileUris!!)
            var fileSelected = File(path)
            imageFile = File(currentPhotoPath)
            type1 = "2"
            imageArray.add(fileSelected.toString())
            val surveyBody =
                fileSelected.asRequestBody("multipart/form-data".toMediaTypeOrNull())

            requestMultiImagesAndVideos.add(
                MultipartBody.Part.createFormData(
                    "hearing_doc[]",
                    fileSelected.name,
                    surveyBody
                )
            )
            notifyAdapterData()
        } else if (requestCode == pickImageCamera && data == null) {


            if (resultCode == RESULT_OK) {
                Log.e(TAG, "onActivityResult:$currentPhotoPath")

                imageFile = File(currentPhotoPath)

                imageArray.add(currentPhotoPath)

                type1 = "1"
                val surveyBody = imageFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                requestMultiImagesAndVideos.add(
                    MultipartBody.Part.createFormData(
                        "hearing_doc[]",
                        imageFile!!.name,
                        surveyBody
                    )
                )
                notifyAdapterData()
            }

        } else if (requestCode == SELECT_PICTURES && data != null) {
            type1 = "1"
            if (resultCode == RESULT_OK) {

                val selectedImage = data.data

                try {
                    val bitmap1 =
                        MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            selectedImage
                        )
                    val bytes1 = ByteArrayOutputStream()
                    bitmap1!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes1)
                    Log.e("Activity", "Pick from Gallery::>>> ")
                    val newBitMap1: Bitmap = CommonMethods.changeImageOrientation(
                        CommonMethods.getRealPathFromUri(
                            this,
                            selectedImage
                        )!!.absolutePath,
                        bitmap1
                    )!!
                    imagePath = CommonMethods.compressImage(
                        CommonMethods.getRealPathFromUri(
                            this,
                            selectedImage
                        )!!.absolutePath
                    ).toString()



                    Log.d("TAG", "onActivityResult: nvnvn  " + imagePath)

                    if (imageArray.size <= 4) {
                        imageArray.add(imagePath)

                        if (imageArray.size >= 0) {

                            setAdapter()

                            if (imagePath.isNotEmpty()) {
                                var fileGallery1: File? = null
                                fileGallery1 = File(imagePath)

                                val surveyBody =
                                    RequestBody.create("image/*".toMediaTypeOrNull(), fileGallery1)
                                requestMultiImagesAndVideos.add(
                                    MultipartBody.Part.createFormData(
                                        "hearing_doc[]",
                                        fileGallery1.name,
                                        surveyBody
                                    )
                                )
                                Log.d(TAG, "onActivityResult: " + requestMultiImagesAndVideos)
                            }
                        }

                    } else {
                    }

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }


            }
            if (data.clipData != null) {
                val cout = data.clipData!!.itemCount

                if (imageArray.size == 0) {
                    if (cout <= 5
                    ) {
                        for (i in 0 until cout) {
                            val imageUrl = data.clipData!!.getItemAt(i).uri


                            val getRealPath = ImageRotation.getRealPathFromURI2(this, imageUrl)
                            val mimeType = getMimeType(getRealPath)
                            imageFile = File(getRealPath!!)
                            imageArray.add(getRealPath)


                            val surveyBody =
                                imageFile!!.asRequestBody(mimeType?.toMediaTypeOrNull())
                            requestMultiImagesAndVideos.add(
                                MultipartBody.Part.createFormData(
                                    "hearing_doc[]",
                                    imageFile!!.name,
                                    surveyBody
                                )
                            )
                        }
                        notifyAdapterData()
                    } else {
                        for (i in 0 until 5) {
                            val imageUrl = data.clipData!!.getItemAt(i).uri


                            val getRealPath = ImageRotation.getRealPathFromURI2(this, imageUrl)

                            val mimeType = getMimeType(getRealPath)
                            imageFile = File(getRealPath!!)
                            imageArray.add(getRealPath)
                            Log.d("TAG", "onActivityResult: " + imageArray)

                            val surveyBody =
                                imageFile!!.asRequestBody(mimeType?.toMediaTypeOrNull())
                            requestMultiImagesAndVideos.add(
                                MultipartBody.Part.createFormData(
                                    "hearing_doc[]",
                                    imageFile!!.name,
                                    surveyBody
                                )
                            )

                        }
                        notifyAdapterData()
                    }
                } else {

                    when (imageArray.size) {
                        4 -> {
                            if (cout <= 1) {
                                for (i in 0 until cout) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )


                                }
                                notifyAdapterData()
                            } else {
                                for (i in 0 until 1) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )
                                }
                                notifyAdapterData()
                            }
                        }
                        3 -> {
                            if (cout <= 2) {


                                for (i in 0 until cout) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )

                                }
                                notifyAdapterData()
                            } else {
                                for (i in 0 until 2) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )
                                }
                                notifyAdapterData()
                            }

                        }


                        2 -> {
                            if (cout <= 3) {
                                for (i in 0 until cout) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )


                                }
                                notifyAdapterData()
                            } else {
                                for (i in 0 until 3) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )
                                }
                                notifyAdapterData()
                            }

                        }
                        1 -> {
                            if (cout <= 4) {
                                for (i in 0 until cout) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri
                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )

                                }
                                notifyAdapterData()

                            } else {

                                for (i in 0 until 4) {
                                    val imageUrl = data.clipData!!.getItemAt(i).uri

                                    val getRealPath =
                                        ImageRotation.getRealPathFromURI2(this, imageUrl)
                                    val mimeType = getMimeType(getRealPath)
                                    imageFile = File(getRealPath!!)
                                    imageArray.add(getRealPath)

                                    val surveyBody = imageFile!!
                                        .asRequestBody(mimeType?.toMediaTypeOrNull())
                                    requestMultiImagesAndVideos.add(
                                        MultipartBody.Part.createFormData(
                                            "hearing_doc[]",
                                            imageFile!!.name,
                                            surveyBody
                                        )
                                    )

                                }
                                notifyAdapterData()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun notifyAdapterData() {
        hearingAdapetr.notifyDataSetChanged()
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url?.trim())

        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Files", "Cancel")
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
            } else if (options[item] == "Files") {
                dialog.dismiss()
                selectPdf()
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun selectPdf() {
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent, 12)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createaddimage(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val addimageName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            addimageName,
            ".jpg",
            storageDir
        )
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath

        return image

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(this.packageManager) != null) {
            try {
                imageFile = createaddimage()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            if (imageFile != null) {
                photoURI = FileProvider.getUriForFile(
                    this,
                    "com.lawyersbuddy.app.myUniquefileprovider",
                    imageFile!!
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, pickImageCamera)
            }
        }

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
            .withErrorListener {
            }
            .onSameThread()
            .check()
    }

    private fun openGallery() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type =
            "image/*" //allows any image file type. Change * to specific extension to limit it
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            SELECT_PICTURES
        )

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
        if (binding.etCaseTitle.text.trim().isEmpty()) {
            binding.etCaseTitle.error = "Please enter your case title"
            binding.etCaseTitle.requestFocus()
            return false
        } else if (binding.etPartyName.text.trim().isEmpty()) {
            binding.etPartyName.error = "Please enter your party name"
            binding.etPartyName.requestFocus()
            return false
        } else if (binding.etCaseStage.text.trim().isEmpty()) {
            binding.etCaseStage.error = "Please enter your case stage"
            binding.etCaseStage.requestFocus()
            return false
        } else if (binding.etCaseStatus.text.trim().isEmpty()) {
            binding.etCaseStatus.error = "Please enter case status"
            binding.etCaseStatus.requestFocus()
            return false
        } else if (binding.etHearingDetails.text.trim().isEmpty()) {
            binding.etHearingDetails.error = "Please enter hearing details"
            binding.etHearingDetails.requestFocus()
            return false
        }
        if (binding.finalhearing.isChecked) {

        } else if (binding.tvNextHearingdate.text.trim().isEmpty()) {
            binding.tvNextHearingdate.error = "Please enter next hearing date"
            binding.tvNextHearingdate.requestFocus()
            return false
        }
        return true
    }

    override fun click(id: Int) {
        imagesmodel.removeAt(id)
    }

    fun open_images() {
        val builder = android.app.AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .create()

        val view = layoutInflater.inflate(R.layout.images_popup, null)
        var images: RecyclerView = view.findViewById(R.id.rv_image)
        var send: ImageView = view.findViewById(R.id.send)

//        recyclerView = HearingImagesAdapter( this,imagesmodel,this)
//        images.adapter = recyclerView
//        recyclerView.notifyDataSetChanged()

        send.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(view)

        builder.setCancelable(true)
        builder.show()

        val window: Window = builder.window!!
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }

    override fun imageclick(position: Int) {
        imageArray.removeAt(position)
        requestMultiImagesAndVideos.removeAt(position)

        hearingAdapetr.notifyItemRemoved(position)
        hearingAdapetr.notifyItemRangeChanged(position, imageArray.size)

    }


    private fun setAdapter() {
        binding.rvImages.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hearingAdapetr = HearingImagesAdapter(this, imageArray, this)
        binding.rvImages.adapter = hearingAdapetr

    }

    private fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            this.contentResolver.query(contentUri!!, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        imagePath = image.absolutePath
        return image
    }

    override fun click(image: String?) {
        supportFragmentManager.let { PopupActivity(image).show(it, "MyCustomFragment") }

    }

    override fun clickpdf(image: String?) {
        TODO("Not yet implemented")
    }

    override fun deleteimage(name: String?) {
        hearingViewModel.remove_hearing_img(
            "Bearer " + userPref.getToken().toString(),
            name!!,
            hearing_id
        )

    }

}