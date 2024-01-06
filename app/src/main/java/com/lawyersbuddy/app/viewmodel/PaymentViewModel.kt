package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.HomeSearchResponse
import com.lawyersbuddy.app.model.LoginResponse
import com.lawyersbuddy.app.model.PaymentListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    var PaymentListResponse = MutableLiveData<PaymentListResponse>()
    val errorString = MutableLiveData<String>()

    val progressBarStatus = MutableLiveData<Boolean>()

    fun PaymentListApi(
        token: String
    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true
                val response = mainRepository.payment_list(
                    token
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    PaymentListResponse.postValue(response.body())
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

