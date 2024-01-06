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
class ClientViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){
    var ClientListResponse = MutableLiveData<ClientListResponse>()
    var ClientDetailsResponse = MutableLiveData<ClientDetailsResponse>()
    var ClientListData =   MutableLiveData<ArrayList<ClientListData>>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var AddClientResponse=MutableLiveData<AddClientResponse>()
    var editClientResponse=MutableLiveData<ClientDetailsResponse>()
    var SearchResponseListModelClass=MutableLiveData<SearchResponseListModelClass>()
    val errorString = MutableLiveData<String>()

    fun ClientListApi(
     token:String,
     search:String
    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true
                val response = mainRepository.client_list(
                    token, search
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    ClientListResponse.postValue(response.body())
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
  fun ClientListSearchApi(
     token:String,
     search:String
    ) {
        viewModelScope.launch {
            try {
                val response = mainRepository.client_list(
                    token, search
                )
                if (response.isSuccessful) {
                    ClientListResponse.postValue(response.body())
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

    fun add_client(
        token: String,
        mobile:String,address : String,name:String,email:String,image:MultipartBody.Part

    ) {
        val mobile: RequestBody = mobile.toRequestBody("text/plain".toMediaTypeOrNull())
        val address: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val email: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            try {

                progressBarStatus.value = true
                val response = mainRepository.add_client(
                    token, mobile, address, name, email, image
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    AddClientResponse.postValue(response.body())
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

    fun client_detail(
        token: String,
        client_id:String

    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.client_detail(
                    token, client_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    ClientDetailsResponse.postValue(response.body())
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

    fun editClient(
        token: String,
        client_id: String,
        mobile:String,address : String,name:String,email:String,image: MultipartBody.Part?

    ) {
        val client_id: RequestBody = client_id.toRequestBody("text/plain".toMediaTypeOrNull())
        val mobile: RequestBody = mobile.toRequestBody("text/plain".toMediaTypeOrNull())
        val address: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val name: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val email: RequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())


        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.editClient(
                    token, client_id, mobile, address, name, email, image!!
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    editClientResponse.postValue(response.body())
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
    fun clientsearchlist(
        token: String,
       search: String

    ) {

        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.client_search(
                    token, search
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
               SearchResponseListModelClass.postValue(response.body())
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


}