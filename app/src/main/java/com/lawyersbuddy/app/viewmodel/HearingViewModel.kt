package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HearingViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    var hearingListResponse = MutableLiveData<HearingListResponse>()
    var deleteimageresponse = MutableLiveData<LoginResponse>()
    //var HearingListData = MutableLiveData<ArrayList<HearingData>()
    val progressBarStatus = MutableLiveData<Boolean>()
    //var AddHearingResponse = MutableLiveData<AddHearingResponse>()
    var hearingDetailsResponse = MutableLiveData<HearingDetailsResponse>()
    var downloadHearingDetailsResponse = MutableLiveData<DownloadPdfResponse>()

    val errorString = MutableLiveData<String>()

    var addHearingResponse=MutableLiveData<AddHearingResponse>()
    var EditHearingResponse=MutableLiveData<EditHearingResponse>()
    val error = MutableLiveData<String?>()


    fun hearing_listApi(
        token: String,search:String,
        searchDate:String
    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true
                val response = mainRepository.hearing_listApi(
                    token, search, searchDate
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                    hearingListResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            } catch (e: ApiException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: NoInternetException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: Exception) {
                progressBarStatus.value = false
                errorString.postValue("No internet connection.")
                e.printStackTrace()
            }
        }
    }
    fun remove_hearing_img(token:String,
        file_name: String,
        hearing_id: String
    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true
                val response = mainRepository.remove_hearing_img(
                    token, file_name,hearing_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                    deleteimageresponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            } catch (e: ApiException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: NoInternetException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: Exception) {
                progressBarStatus.value = false
                errorString.postValue("No internet connection.")
                e.printStackTrace()
            }
        }
    }
    fun hearing_listSearchApi(
        token: String,search:String,
        searchDate:String
    ) {
        viewModelScope.launch {

            try {
                val response = mainRepository.hearing_listApi(
                    token, search, searchDate
                )
                if (response.isSuccessful) {
//                Log.d("TAG", response.body().toString())
                    hearingListResponse.postValue(response.body())
                } else {
                    Log.d("TAG", response.body().toString())
                }
            } catch (e: ApiException) {
                errorString.postValue(e.message!!)
            } catch (e: NoInternetException) {
                errorString.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                errorString.postValue(e.message!!)
            } catch (e: Exception) {
                errorString.postValue("No internet connection.")
                e.printStackTrace()
            }
        }
    }


    fun hearing_detailApi(
        token: String,
        client_id:String

    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.hearing_detailApi(
                    token, client_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    hearingDetailsResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            } catch (e: ApiException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: NoInternetException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            }
//            catch (e: Exception) {
//                progressBarStatus.value = false
//                errorString.postValue("No internet connection.")
//                e.printStackTrace()
//            }
        }
        }

    fun downloadHearingDetailApi(
        token: String,
        hearing_id:String

    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.downloadHearingDetailApi(
                    token, hearing_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    downloadHearingDetailsResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            } catch (e: ApiException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: NoInternetException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            } catch (e: SocketTimeoutException) {
                progressBarStatus.value = false
                errorString.postValue(e.message!!)
            }
//            catch (e: Exception) {
//                progressBarStatus.value = false
//                errorString.postValue("No internet connection.")
//                e.printStackTrace()
//            }
        }
    }
 fun EditHearingApi(
        token:String,hearing_id:  String,case_stage: String,case_status: String
        ,current_date: String,hearing_details: String
        ,hearing_date: String,final_date: String,is_auto_reminder:String,doc_type:String,hearing_doc:ArrayList<MultipartBody.Part?>
    ) {

     val hearing_id: RequestBody = hearing_id.toRequestBody("text/plain".toMediaTypeOrNull())
     val case_stage: RequestBody = case_stage.toRequestBody("text/plain".toMediaTypeOrNull())
     val case_status: RequestBody = case_status.toRequestBody("text/plain".toMediaTypeOrNull())
     val current_date: RequestBody = current_date.toRequestBody("text/plain".toMediaTypeOrNull())
     val hearing_details: RequestBody = hearing_details.toRequestBody("text/plain".toMediaTypeOrNull())
     val hearing_date: RequestBody = hearing_date.toRequestBody("text/plain".toMediaTypeOrNull())
     val final_date: RequestBody = final_date.toRequestBody("text/plain".toMediaTypeOrNull())
     val is_auto_reminder: RequestBody = is_auto_reminder.toRequestBody("text/plain".toMediaTypeOrNull())
     val doc_type1: RequestBody = doc_type.toRequestBody("text/plain".toMediaTypeOrNull())

     viewModelScope.launch {
         try {
             progressBarStatus.value = true
             val response =

                 mainRepository.edit_hearingApi(
                     token,
                     hearing_id,
                     case_stage,
                     case_status,
                     current_date,
                     hearing_details,
                     hearing_date,
                     final_date,
                     is_auto_reminder,doc_type1,
                     hearing_doc

                 )

             if (response!!.isSuccessful) {
                 progressBarStatus.value = false

                     EditHearingResponse.postValue(response.body())
                 } else {
                 progressBarStatus.value = false
                 error.postValue(response.body()!!.message)
                 }

         } catch (e: ApiException) {
             progressBarStatus.value = false
             errorString.postValue(e.message!!)
         } catch (e: NoInternetException) {
             progressBarStatus.value = false
             errorString.postValue(e.message!!)
         } catch (e: SocketTimeoutException) {
             progressBarStatus.value = false
             errorString.postValue(e.message!!)
         } catch (e: Exception) {
             progressBarStatus.value = false
             errorString.postValue(e.message!!)

//             errorString.postValue("No internet connection.")
             e.printStackTrace()
         }

     }
 }


}
