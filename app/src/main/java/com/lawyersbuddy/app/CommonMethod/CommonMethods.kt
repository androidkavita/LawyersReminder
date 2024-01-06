package com.lawyersbuddy.app.CommonMethod

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.lawyersbuddy.app.R

import java.io.*

@SuppressLint("StaticFieldLeak")
object CommonMethods {
         lateinit var mContext: Context
        operator fun invoke(applicationContext: Context?) {
            this.mContext = applicationContext!!
        }





    //define functions for each permission
    const val PER_CAMERA = Manifest.permission.CAMERA
    const val PER_WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    const val PER_READ_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

    fun checkPermission(
        context: Context?,
        permission: String?
    ): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isCameraGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_CAMERA
        )
    }

    fun isWriteStorageGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_WRITE_STORAGE
        )
    }


    fun phoneIsOnline(context: Context): Boolean {
        val cm =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    //this method return the phone is Connected with internet or Not
    fun phoneIsOnline(): String? {
        val cm =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnectedOrConnecting) {
            "Something went wrong. Please try again later."
        } else {
            "Internet connection not available!"
        }
    }

    fun isReadStorageGranted(context: Context?): Boolean {
        return checkPermission(
            context,
            PER_READ_STORAGE
        )
    }

    fun requestPermissions(
        activity: Activity?,
        permissionId: Int,
        vararg permissions: String?
    ) {
        ActivityCompat.requestPermissions(activity!!, permissions, permissionId)
    }

    fun createImageFile(mContext: Context): File {
        val imageFileName = "Dive App" + "_" + System.currentTimeMillis()
        val storageDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "Dive App")
        }

        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        var image: File
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
            val newImageFileName = "Dive App" + "-" + System.currentTimeMillis() + ".jpg"
            image = File(storageDir, newImageFileName)
            try {
                image.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return image
    }


    fun changeImageOrientation(photoPath: String?, bitmap: Bitmap): Bitmap? {
        var ei: ExifInterface? = null
        try {
            ei = ExifInterface(photoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = 0
        if (ei != null) {
            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
        }
        val rotatedBitmap: Bitmap
        rotatedBitmap = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(
                bitmap,
                90f
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180f
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270f
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
    fun getResizedBitmap(
        bitmapImage: Bitmap?,
        bitmapWidth: Int,
        bitmapHeight: Int
    ): Bitmap? {
        return Bitmap.createScaledBitmap(bitmapImage!!, bitmapWidth, bitmapHeight, true)
    }

    fun createFileFromBitMap(bitmap: Bitmap): File? {
        val file: File
        val imageFileName =
            "Joos App" + "-" + System.currentTimeMillis() + ".jpg"
        val myDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "Joos App")
        }
        if (!myDirectory.exists()) {
            myDirectory.mkdir()
        }
        file = File(myDirectory, imageFileName)
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        //write the bytes in file
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }

    fun getRealPathFromUri(context: Context, uri: Uri?): File? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)
            val fileName: String = getFileName(context, uri)!!
            val splitName: Array<String> = splitFileName(fileName)!!
            var tempFile = File.createTempFile(splitName[0], splitName[1])
            tempFile = rename(tempFile, fileName)
            tempFile.deleteOnExit()
            val out = FileOutputStream(tempFile)
            if (inputStream != null) {
                copy(inputStream, out)
                inputStream.close()
            }
            out.close()
            return tempFile
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    private fun copy(input: InputStream, output: OutputStream) {
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
        }
    }

    private fun rename(file: File, newName: String): File? {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }
    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            if (result != null) {
                val cut = result.lastIndexOf(File.separator)
                if (cut != -1) {
                    result = result.substring(cut + 1)
                }
            }
        }
        return result
    }

    private fun splitFileName(fileName: String): Array<String>? {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    fun compressImage(filePath: String?): String? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

// by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
// you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

// max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

// width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

// setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

// inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

// this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
// load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

// check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

// write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight;
        val width = options.outWidth;
        var inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio
            } else {
                inSampleSize = widthRatio
            }

        }
        var totalPixels = width * height;
        var totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
    private fun getFilename(): String? {
        val file = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
// val file = File(Environment.getExternalStorageDirectory().path, ".spongy/Images")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + System.currentTimeMillis() + ".jpg"
    }

    @SuppressLint("CheckResult")
    fun imageProgressBar(context: Context): CircularProgressDrawable {
        val circularProgressDrawable =
            CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }





    fun compressVideo(filePath: String?): String? {
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()

// by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
// you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

// max Height and width values of the compressed image is taken as 816x612
        val maxHeight = 1024.0f
        val maxWidth = 1024.0f
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = maxWidth / maxHeight

// width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }

// setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)

// inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false

// this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
// load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )

// check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap, 0, 0,
                scaledBitmap.width, scaledBitmap.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val out: FileOutputStream
        val filename = getFilename()
        try {
            out = FileOutputStream(filename)

// write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }


}