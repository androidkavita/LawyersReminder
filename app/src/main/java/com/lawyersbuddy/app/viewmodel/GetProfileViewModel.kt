package com.lawyersbuddy.app.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawyersbuddy.app.R

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.GetProfileResponseModel
import com.lawyersbuddy.app.model.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class GetProfileViewModel@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var profileViewResponse= MutableLiveData<GetProfileResponseModel>()
    var editProfileViewResponse= MutableLiveData<LoginResponse>()
    val error = MutableLiveData<String?>()
    val errorString = MutableLiveData<String>()

    val progressBarStatus = MutableLiveData<Boolean>()
    fun get_profileApi(
     token: String
    ) {
            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.get_profileApi(
                        token
                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                        profileViewResponse.postValue(response.body())
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
    fun delete_user(
        token:String,
     id: String
    ) {
            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.delete_user(token ,
                        id
                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
                        editProfileViewResponse.postValue(response.body())
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

    fun editProfileApi(
        token: String,
        name: String,
        state: String,
        city: String,
        bar_association: String,
        bar_council_number: String,
        firm_name: String,
        zip_code: String,
        address: String,
        image: MultipartBody.Part?
    ) {

        if (TextUtils.isEmpty(name.trim())) {
            error.postValue(R.string.please_enter_name.toString())
            return
        } else if (TextUtils.isEmpty(state.trim())) {
            error.postValue(R.string.please_enter_state.toString())
            return
        } else if (TextUtils.isEmpty(city.trim())) {
            error.postValue(R.string.please_enter_city.toString())
            return
        } else if (TextUtils.isEmpty(bar_association.trim())) {
            error.postValue(R.string.please_enter_bar_association.toString())
            return
        } else if (TextUtils.isEmpty(bar_council_number.trim())) {
            error.postValue(R.string.please_enter_bar_council_number.toString())
            return
        }

        val username: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val userstate: RequestBody = state.toRequestBody("text/plain".toMediaTypeOrNull())
        val usercity: RequestBody = city.toRequestBody("text/plain".toMediaTypeOrNull())
        val userbar_association: RequestBody =
            bar_association.toRequestBody("text/plain".toMediaTypeOrNull())
        val userbar_council_number: RequestBody =
            bar_council_number.toRequestBody("text/plain".toMediaTypeOrNull())
        val useraddress: RequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val zip_code: RequestBody = zip_code.toRequestBody("text/plain".toMediaTypeOrNull())
        val firm_name: RequestBody = firm_name.toRequestBody("text/plain".toMediaTypeOrNull())


        viewModelScope.launch {

            try {

                progressBarStatus.value = true
                val response =
                    image.let {
                        mainRepository.editProfileApi(
                            token, username, userstate, usercity, userbar_association,
                            userbar_council_number, firm_name, zip_code, useraddress, it!!
                        )
                    }
                if (response.isSuccessful) {
                    if (response.body()?.status == 1) {

                        editProfileViewResponse.postValue(response.body())
                    } else {
                        error.postValue(response.body()?.message)
                    }
                    progressBarStatus.value = false

                } else {
                    error.postValue(response.message())
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