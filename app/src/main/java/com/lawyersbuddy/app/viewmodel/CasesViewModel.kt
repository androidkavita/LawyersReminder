package com.lawyersbuddy.app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.lawyersbuddy.app.data.ApiException
import com.lawyersbuddy.app.data.MainRepository
import com.lawyersbuddy.app.data.NoInternetException
import com.lawyersbuddy.app.model.AddCaseResponse
import com.lawyersbuddy.app.model.CaseDetailsResponse
import com.lawyersbuddy.app.model.CasesData
import com.lawyersbuddy.app.model.CasesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class CasesViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){
    var CasesListResponse = MutableLiveData<CasesResponse>()
    var CasesDetailsResponse = MutableLiveData<CaseDetailsResponse>()
    var downloadCaseDetailResponse = MutableLiveData<CaseDetailsResponse>()
    var CasesListData =   MutableLiveData<ArrayList<CasesData>>()
    val progressBarStatus = MutableLiveData<Boolean>()
    var addCasesResponse= MutableLiveData<AddCaseResponse>()
    val errorString = MutableLiveData<String>()

    fun CasesListApi(
        token:String,search:String,searh_date:String
    ) {

            viewModelScope.launch {
                try {
                    progressBarStatus.value = true

                    val response = mainRepository.case_list(
                        token,search,searh_date
                    )
                    if (response.isSuccessful) {
                        progressBarStatus.value = false
                        CasesListResponse.postValue(response.body())
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
                    e.printStackTrace()
                }
            }
    }
 fun CasesListSearchApi(
        token:String,search:String,searh_date:String
    ) {

            viewModelScope.launch {
                try {

                    val response = mainRepository.case_list(
                        token,search,searh_date
                    )
                    if (response.isSuccessful) {
                        CasesListResponse.postValue(response.body())
                    } else {
                        Log.d("TAG", response.body().toString())
                    }
                }

                catch (e: ApiException) {
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

    fun add_caseApi(
        token: String,
        start_date:String,first_hearing_date : String,opponent_party_name:String,
        case_stage:String,case_status : String,judge_name:String,cnr_number:String,
        court_name:String,case_title : String,case_detail:String,client_id:String,
        opponent_party_lawyer_name:String

    ) {
        viewModelScope.launch {

        try {
            progressBarStatus.value = true
                val response = mainRepository.add_caseApi(token,start_date,first_hearing_date,opponent_party_name,
                    case_stage ,case_status , judge_name, cnr_number,court_name ,case_title ,case_detail ,client_id,
                    opponent_party_lawyer_name
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    addCasesResponse.postValue(response.body())
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
            e.printStackTrace()
        }
        }
    }

    fun case_detail(
        token: String,
        case_id:String

    ) {
        viewModelScope.launch {
        try {

                progressBarStatus.value = true
                val response = mainRepository.case_detail(token,case_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    CasesDetailsResponse.postValue(response.body())
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
            e.printStackTrace()
        }
        }
    }
    fun edit_case(
        token: String,
        case_id: String,
        start_date:String,opponent_party_name:String,
        case_stage:String,case_status : String,judge_name:String,cnr_number:String,
        court_name:String,case_title : String,case_detail:String,
        opponent_party_lawyer_name:String

    ) {
        viewModelScope.launch {
            try {

                progressBarStatus.value = true

                val response = mainRepository.edit_case(
                    token,
                    case_id,
                    start_date,
                    opponent_party_name,
                    case_stage,
                    case_status,
                    judge_name,
                    cnr_number,
                    court_name,
                    case_title,
                    case_detail,
                    opponent_party_lawyer_name
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    addCasesResponse.postValue(response.body())
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

    fun downloadPdfApi(
        token: String,
        case_id:String

    ) {
        progressBarStatus.value = true
        try {
            viewModelScope.launch {
                val response = mainRepository.downloadCaseDetailApi(token,case_id
                )
                if (response.isSuccessful) {
                    progressBarStatus.value = false
                    downloadCaseDetailResponse.postValue(response.body())
                } else {
                    progressBarStatus.value = false
                    Log.d("TAG", response.body().toString())
                }
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
            e.printStackTrace()
        }
    }
}