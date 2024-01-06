package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.OffersResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class OffersViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){
    var offersResponse = MutableLiveData<OffersResponseModel>()
    val progressBarStatus = MutableLiveData<Boolean>()
    val errorString = MutableLiveData<String>()

    fun coupon_listApi(
        token:String
    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true
                val response = mainRepository.coupon_listApi(
                    token
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                    offersResponse.postValue(response.body())
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