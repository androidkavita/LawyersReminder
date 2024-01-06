package com.lawyersbuddy.app.util

import android.content.Context
import android.graphics.Bitmap
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.lawyersbuddy.app.R
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object CommonUtils {

//    fun setFragment(fragment: Fragment, removeStack: Boolean, activity: FragmentActivity, mContainer: Int) {
//        val fragmentManager = activity.supportFragmentManager
//        val ftTransaction = fragmentManager.beginTransaction()
//        if (removeStack) {
//            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//            ftTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
//            ftTransaction.replace(mContainer, fragment)
//            //ftTransaction.addToBackStack(null)
//        } else {
//            ftTransaction.replace(mContainer, fragment)
//            //ftTransaction.addToBackStack(null)
//        }
//        ftTransaction.commit()
//    }
fun setFragment(fragment: Fragment, removeStack: Boolean, activity: FragmentActivity, mContainer: Int) {
    val fragmentManager = activity.supportFragmentManager
    val ftTransaction = fragmentManager.beginTransaction()
    if (removeStack) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        ftTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
        ftTransaction.addToBackStack(null)
        ftTransaction.replace(mContainer, fragment)
        //ftTransaction.addToBackStack(null)
    } else {
        ftTransaction.replace(mContainer, fragment)
        //ftTransaction.addToBackStack(null)
    }
    ftTransaction.commit()
}

    fun addFragment(fragment: Fragment,activity: FragmentActivity, mContainer: Int){
        val fragmentManager = activity.supportFragmentManager
        val ftTransaction = fragmentManager.beginTransaction()
        ftTransaction.add(mContainer,fragment)
        ftTransaction.commit()
    }

    fun getNumberOfFragmentInContainer(activity: FragmentActivity) : Int{
        val fragmentManager = activity.supportFragmentManager
        return fragmentManager.backStackEntryCount
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
    fun createFileFromBitMap(bitmap: Bitmap): File? {
        val file: File
        val imageFileName =
            "Lawyer's Buddy" + "-" + System.currentTimeMillis() + ".jpg"
        val myDirectory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Pictures"
            )
        } else {
            File(Environment.getExternalStorageDirectory(), "Sham App")
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
                90
            )
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(
                bitmap,
                180
            )
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(
                bitmap,
                270
            )
            ExifInterface.ORIENTATION_NORMAL -> bitmap
            else -> bitmap
        }
        return rotatedBitmap
    }

 /*   fun getCurrentFragment(activity: FragmentActivity) : Fragment?{
        val fragmentManager = activity.supportFragmentManager
        return fragmentManager.findFragmentById(R.id.frame_Container)
    }*/

    /*To convert string date from one format to another format*/
    fun getDate(currentFormat: String, requiredFormat: String, dateString: String): String? {
        var result = ""
        if (dateString.isNullOrEmpty()) {
            return result
        }
        val formatterOld =
            SimpleDateFormat(currentFormat, Locale.getDefault())
        val formatterNew =
            SimpleDateFormat(requiredFormat, Locale.getDefault())
        var date: Date? = null
        try {
            date = formatterOld.parse(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (date != null) {
            result = formatterNew.format(date)
        }
        return result
    }

    fun isValidMail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN: String =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()

    }
    fun String.containsAnyOfIgnoreCase(keywords: List<String>): Boolean {
        for (keyword in keywords) {
            if (this.contains(keyword, true)) return true
        }
        return false
    }


    fun getDate_format(getDateStamp: String, type: Int): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = format.parse(getDateStamp)
//"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        var change_date_format=android.text.format.DateFormat.format("dd-MM-yyyy",date)as String
        val year = android.text.format.DateFormat.format("yyyy", date) as String
//
        return if (type == 0) {
            change_date_format

        } else {
            year
        }
    }

}