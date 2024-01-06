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
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    var SignupResponseModel = MutableLiveData<SignupResponse>()
    var OtpResponse = MutableLiveData<OtpResponse>()
    var ContactUsResponse = MutableLiveData<ContactUsResponse>()
    var AboutUsResponse = MutableLiveData<AboutUsResponse>()
    var LoginResponse = MutableLiveData<LoginResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()
    val errorString = MutableLiveData<String>()



    fun ContactUsApi(

    ) {
        progressBarStatus.value = true

            viewModelScope.launch {
                try {
                    val response = mainRepository.ContactUS(

                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
                        ContactUsResponse.postValue(response.body())
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
    fun add_contact(
        message: String,name:String,email:String
    ) {
        progressBarStatus.value = true
        try {
            viewModelScope.launch {
                val response = mainRepository.add_contact(message,name,email
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    ContactUsResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
            }
        } catch (e: Exception) {
            progressBarStatus.value = false
            e.printStackTrace()

        }
    }
    fun aboutusApi(

    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true

                val response = mainRepository.aboutus(

                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    AboutUsResponse.postValue(response.body())
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
    fun term_conditionApi(

    ) {

            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.term_condition(

                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
                        AboutUsResponse.postValue(response.body())
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
    fun withusApi(

    ) {
        viewModelScope.launch {
        try {
            progressBarStatus.value = true
            val response = mainRepository.withus(
            )
            if (response.isSuccessful) {
                progressBarStatus.value = false
                AboutUsResponse.postValue(response.body())
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

            }
        }
    }
}