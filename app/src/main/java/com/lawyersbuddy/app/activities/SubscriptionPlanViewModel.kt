package com.lawyersbuddy.app.activities

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.LoginResponse
import com.lawyersbuddy.app.model.SubscriptionDetailResponse
import com.lawyersbuddy.app.model.SubscriptionPlanResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject


@HiltViewModel
class SubscriptionPlanViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    val subscriptionPlanResponse = MutableLiveData<SubscriptionPlanResponse>()
    val subscriptionPlanDetailResponse = MutableLiveData<SubscriptionDetailResponse>()
    var PaymentResponse = MutableLiveData<LoginResponse>()

    //    val buySubscriptionPlanResponse = MutableLiveData<CommonResponse>()
    val errorString = MutableLiveData<String>()

    val progressBarStatus = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()


    fun subscriptionPlanApi(
        token: String,

        ) {

        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.subscriptionListApi(
                    token,
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    subscriptionPlanResponse.postValue(response.body())
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

    fun subscriptionPlanDetailApi(
        token: String,
        plan_id: String

    ) {


        viewModelScope.launch {
            try {
                progressBarStatus.value = true
                val response = mainRepository.subscriptionDetailApi(
                    token, plan_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    subscriptionPlanDetailResponse.postValue(response.body())
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
    fun PaymentApi(token: String,
        subscription_id: String,
        transaction_id: String,
        amount: String,
        validity: String,
        payment_created: String
    ) {
        viewModelScope.launch {

            try {
                progressBarStatus.value = true
                val response = mainRepository.payment(token,
                    subscription_id,transaction_id,amount,validity,payment_created
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    PaymentResponse.postValue(response.body())
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
    /*

     fun buySubscriptionPlanApi(
         token: String,
         plan_id: String

     ) {
         progressBarStatus.value = true
         viewModelScope.launch {
             val response = mainRepository.buySubscriptionPlanApi(
                 token, plan_id
             )
             if (response.isSuccessful) {
                 progressBarStatus.value = false
                 buySubscriptionPlanResponse.postValue(response.body())
             } else {
                 progressBarStatus.value = false
                 Log.d("TAG", response.body().toString())
             }
         }
     }*/
