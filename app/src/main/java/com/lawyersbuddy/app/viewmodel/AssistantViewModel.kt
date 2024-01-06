package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.AddAssistantResponse
import com.lawyersbuddy.app.model.AssistantListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    var AssistantListResponse = MutableLiveData<AssistantListResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var AddAssistantResponse = MutableLiveData<AddAssistantResponse>()
    val error = MutableLiveData<String?>()
    val errorString = MutableLiveData<String>()
    fun AssistantListApi(
        token: String,
        search: String
    ) {


            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.assistant_list(
                        token, search
                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                        AssistantListResponse.postValue(response.body())
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
    fun AssistantListSearchApi(
        token: String,
        search: String
    ) {


            viewModelScope.launch {
                try {

                    val response = mainRepository.assistant_list(
                        token, search
                    )
                    if (response.isSuccessful) {
//                Log.d("TAG", response.body().toString())
                        AssistantListResponse.postValue(response.body())
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

    fun add_assistant(
        token: String,
        mobile: String, password: String,
        name: String, email: String, is_permission: String,image:MultipartBody.Part?

    ) {
        val mobile: RequestBody = mobile.toRequestBody("text/plain".toMediaTypeOrNull())
        val password: RequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val email: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val is_permission: RequestBody = is_permission.toRequestBody("text/plain".toMediaTypeOrNull())
        viewModelScope.launch {
        try {

            progressBarStatus.value = true

            val response = mainRepository.add_assistant(
                token, mobile, password, name, email, is_permission, image!!
            )
            if (response.isSuccessful) {
                progressBarStatus.value = false
                AddAssistantResponse.postValue(response.body())
            } else {
                progressBarStatus.value = false
                Log.d("TAG", response.body().toString())
            }

        }catch (e: ApiException) {
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

    fun editAssistantApi(
        token: String,
        assistantId: String,
        mobile: String, password: String,
        name: String, email: String, is_permission: String,image: MultipartBody.Part?

    ) {


            viewModelScope.launch {
                try {
                    progressBarStatus.value = true
                val mobile: RequestBody = mobile.toRequestBody("text/plain".toMediaTypeOrNull())
                val assistantId: RequestBody = assistantId.toRequestBody("text/plain".toMediaTypeOrNull())
                val password: RequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())
                val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val email: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val is_permission: RequestBody = is_permission.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = mainRepository.editAssistant(
                    token, assistantId, mobile, password, name, email, is_permission,image!!
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    AddAssistantResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }

        }  catch (e: ApiException) {
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
        }}
    }

    fun assistantDetailApi(
        token: String,
        assistantId: String,


        ) {
        viewModelScope.launch {
        try {


                progressBarStatus.value = true
                val response = mainRepository.assistantDetailApi(
                    token, assistantId
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    AddAssistantResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            }
         catch (e: ApiException) {
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


}
