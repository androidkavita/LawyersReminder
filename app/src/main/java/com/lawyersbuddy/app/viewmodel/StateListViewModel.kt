package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.CityListData
import com.lawyersbuddy.app.model.CityListResponse
import com.lawyersbuddy.app.model.StateListData
import com.lawyersbuddy.app.model.StateListResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class StateListViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){

    var StateListResponseModel = MutableLiveData<StateListResponse>()
    var StateListData =   MutableLiveData<ArrayList<StateListData>>()
    var CityListResponseModel =  MutableLiveData<CityListResponse>()
    var CityListData =   MutableLiveData<ArrayList<CityListData>>()
    val errorString = MutableLiveData<String>()
    val progressBarStatus = MutableLiveData<Boolean>()
    fun StateListApi(

    ) {
        viewModelScope.launch {
            try {

                progressBarStatus.value = true
                val response = mainRepository.state_list(

                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                    StateListResponseModel.postValue(response.body())
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
                e.printStackTrace()
            }
        }
    }
    fun city_list(
        state_id:String
    ) {
        viewModelScope.launch {
            try {

                progressBarStatus.value = true
                val response = mainRepository.city_list(
                    state_id

                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
//                Log.d("TAG", response.body().toString())
                    CityListResponseModel.postValue(response.body())
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
                e.printStackTrace()
            }
        }

    }

}