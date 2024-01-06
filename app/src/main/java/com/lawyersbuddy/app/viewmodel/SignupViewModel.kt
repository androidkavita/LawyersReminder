package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.LoginResponse
import com.lawyersbuddy.app.model.OtpResponse
import com.lawyersbuddy.app.model.SignupResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){
    var SignupResponseModel = MutableLiveData<SignupResponse>()
    var OtpResponse = MutableLiveData<OtpResponse>()
    var LoginResponse = MutableLiveData<LoginResponse>()
    var user_detailResponse = MutableLiveData<LoginResponse>()
    var LoginResponse_google = MutableLiveData<LoginResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()
    val errorString = MutableLiveData<String>()

    fun SignupApi(
       name: String,
       email : String,
       password: String,
       mobile : String,
       city : String,
       state : String,
       bar_association : String,
       bar_council_number : String,
       termcondition : String,
       firm_name: String,
       address : String
    ) {

            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.signup(
                        name,
                        email,
                        password,
                        mobile,
                        city,
                        state,
                        bar_association,
                        bar_council_number,
                        termcondition,
                        firm_name,
                        address
                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
                        SignupResponseModel.postValue(response.body())
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
    fun send_otp(

       mobile : String

    ) {

        viewModelScope.launch {

            try {
                progressBarStatus.value = true
                val response = mainRepository.send_otp(
                    mobile
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    OtpResponse.postValue(response.body())
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
    fun login(
        email:String,password : String,type:String,token:String
    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true

                val response = mainRepository.login(
                    email, password, type,token
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    LoginResponse.postValue(response.body())
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
fun user_detail(
        email:String

    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true

                val response = mainRepository.user_detail(
                    email
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    user_detailResponse.postValue(response.body())
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

    fun google_login(
        type: String,
        email: String,
        password: String,
        name: String,
        device_id: String,
        device_token: String

    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true

                val response = mainRepository.google_login(type,
                    email,
                    password,
                    name,
                    device_id,
                    device_token
                    )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    LoginResponse.postValue(response.body())
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

    fun forgot_password(
        mobile:String,password : String

    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.forgot_password(
                    mobile, password
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    LoginResponse.postValue(response.body())
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