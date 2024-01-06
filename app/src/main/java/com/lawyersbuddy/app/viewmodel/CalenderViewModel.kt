package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.CalenderListResponse
import com.lawyersbuddy.app.model.DownloadTodayEventResponse
import com.lawyersbuddy.app.model.HomeSearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CalenderViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    var CasesListResponse = MutableLiveData<CalenderListResponse>()
    var DownloadTodayEventResponse = MutableLiveData<DownloadTodayEventResponse>()
    var HomeSearchResponse = MutableLiveData<HomeSearchResponse>()
    val errorString = MutableLiveData<String>()
    val progressBarStatus = MutableLiveData<Boolean>()

    fun CalenderListApi(
        token: String, date: String
    ) {
        viewModelScope.launch {
        try {
            progressBarStatus.value = true
                val response = mainRepository.calendar_list(
                    token, date
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    CasesListResponse.postValue(response.body())
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
            }
        }
    }
    fun download_all_events(
        token: String,
        date: String,
        lawyer_id: String
    ) {
        viewModelScope.launch {
        try {
            progressBarStatus.value = true
                val response = mainRepository.download_all_events(
                    token, date,lawyer_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    DownloadTodayEventResponse.postValue(response.body())
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
            }
        }
    }

    fun home_searchApi(

        token: String,
        search_name: String,
        start_date: String,
        end_date: String,
        search_type: String
    ) {
        viewModelScope.launch {
            try {
                progressBarStatus.value = true

                val response = mainRepository.home_searchApi(
                    token, search_name,start_date,end_date,search_type
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    HomeSearchResponse.postValue(response.body())
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