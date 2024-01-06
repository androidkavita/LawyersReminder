package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.ChangePasswordResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){
    var changePasswordResponseModel = MutableLiveData<ChangePasswordResponse>()
    val progressBarStatus = MutableLiveData<Boolean>()
    val errorString = MutableLiveData<String>()

    fun changePasswordApi(
       token: String,
       old_password : String,
       new_password: String
    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true
                val response = mainRepository.changePasswordApi(
                    token, old_password, new_password
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    changePasswordResponseModel.postValue(response.body())
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