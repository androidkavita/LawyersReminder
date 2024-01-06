package com.lawyersbuddy.app.data


import com.lawyersbuddy.app.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface MainRepository {

    suspend fun signup(
        name: String, email: String, password: String, mobile: String,
        city: String, state: String, bar_association: String,
        bar_council_number: String, termcondition: String,
        firm_name: String, address: String
    ): Response<SignupResponse>

    suspend fun add_client(
        token: String, mobile: RequestBody, address: RequestBody, name: RequestBody, email: RequestBody,image: MultipartBody.Part
    ): Response<AddClientResponse>

    suspend fun editClient(
        token: String,client_id:RequestBody, mobile: RequestBody, address: RequestBody, name: RequestBody, email: RequestBody,image: MultipartBody.Part    ): Response<ClientDetailsResponse>

    suspend fun add_assistant(
        token: String,
        mobile: RequestBody,
        password: RequestBody,
        name: RequestBody,
        email: RequestBody,
        is_permission: RequestBody,image:MultipartBody.Part
    ): Response<AddAssistantResponse>

    suspend fun editAssistant(
        token: String,
        assistant_id:RequestBody,
        mobile: RequestBody,
        password: RequestBody,
        name: RequestBody,
        email: RequestBody,
        is_permission: RequestBody,image: MultipartBody.Part
    ): Response<AddAssistantResponse>

    suspend fun client_detail(
        token: String, client_id: String
    ): Response<ClientDetailsResponse>

    suspend fun assistantDetailApi(
        token: String, assistant_id: String
    ): Response<AddAssistantResponse>

    suspend fun case_detail(
        token: String, case_id: String
    ): Response<CaseDetailsResponse>

    suspend fun assistant_list(
        token: String,
        search: String
    ): Response<AssistantListResponse>

    suspend fun notification_list(
        token: String
    ): Response<NotificationResponse>

    suspend fun ContactUS(): Response<ContactUsResponse>
    suspend fun aboutus(): Response<AboutUsResponse>
    suspend fun term_condition(): Response<AboutUsResponse>
    suspend fun withus(): Response<AboutUsResponse>
    suspend fun add_contact( message: String,name:String,email:String): Response<ContactUsResponse>

    suspend fun state_list(): Response<StateListResponse>
    suspend fun city_list(state_id: String): Response<CityListResponse>
    suspend fun client_list(token: String,search:String): Response<ClientListResponse>
    suspend fun case_list(token: String,search: String,searh_date:String): Response<CasesResponse>
    suspend fun home(token: String): Response<HomeResponse>
    suspend fun send_otp(mobile: String): Response<OtpResponse>
    suspend fun login(email: String, password: String, type: String,token:String): Response<LoginResponse>
    suspend fun google_login(
    type: String,
    email: String,
    google_id: String,
    name: String,
    device_id: String,
    device_token: String): Response<LoginResponse>
    suspend fun forgot_password(mobile: String, password: String): Response<LoginResponse>
    suspend fun delete_user(token: String,id: String): Response<LoginResponse>
     suspend fun remove_hearing_img(
         token: String,
        file_name: String,
        hearing_id: String,
    ): Response<LoginResponse>
    suspend fun user_detail(email: String): Response<LoginResponse>
    suspend fun payment(
                             token: String,
                                  subscription_id: String,
                               transaction_id: String,
                               amount: String,
                               validity: String,
                               payment_created: String): Response<LoginResponse>

    suspend fun changePasswordApi(
        token: String,
        old_password: String,
        new_password: String
    ): Response<ChangePasswordResponse>
    suspend fun download_all_events(
        token: String,
        date: String,
        lawyer_id: String
    ): Response<DownloadTodayEventResponse>
    suspend fun notification_on_off(
        token: String,
        type: String,
        user_id: String
    ): Response<LoginResponse>

    suspend fun get_profileApi(token: String): Response<GetProfileResponseModel>
    suspend fun subscriptionListApi(token: String): Response<SubscriptionPlanResponse>
    suspend fun subscriptionDetailApi(token: String,subscription_id:String): Response<SubscriptionDetailResponse>
    suspend fun coupon_listApi(token: String): Response<OffersResponseModel>
    suspend fun calendar_list(token: String, date: String): Response<CalenderListResponse>
    suspend fun payment_list(token: String): Response<PaymentListResponse>
    suspend fun notification_on_off_list(token: String): Response<LoginResponse>
    suspend fun home_searchApi(token: String,  search_name: String,
                               start_date: String,
                               end_date: String,
                               search_type: String): Response<HomeSearchResponse>
    suspend fun editProfileApi(
        token: String,
        name: RequestBody,
        state: RequestBody,
        city: RequestBody,
        bar_association: RequestBody,
        bar_council_number: RequestBody,
        firm_name: RequestBody,
        zip_code: RequestBody,
        address: RequestBody,
        image: MultipartBody.Part
    ): Response<LoginResponse>

    suspend fun edit_hearingApi(
        token: String,
        hearing_id: RequestBody,
        case_stage: RequestBody,
        case_status: RequestBody,
        current_date: RequestBody,
        hearing_details: RequestBody,
        hearing_date: RequestBody,
        final_date: RequestBody,
        is_auto_reminder: RequestBody,
        doc_type: RequestBody,
        hearing_doc:ArrayList<MultipartBody.Part?>
    ): Response<EditHearingResponse>

    suspend fun hearing_listApi(token: String,search: String,searh_date: String): Response<HearingListResponse>
    suspend fun hearing_detailApi(
        token: String,
        hearing_id: String
    ): Response<HearingDetailsResponse>

    suspend fun downloadCaseDetailApi(
        token: String,
        case_id: String
    ): Response<CaseDetailsResponse>
    suspend fun downloadHearingDetailApi(
        token: String,
        hearing_id: String
    ): Response<DownloadPdfResponse>

    suspend fun edit_case(
        token: String, case_id: String, start_date: String,
        opponent_party_name: String, case_stage: String,
        case_status: String, judge_name: String, cnr_number: String, court_name: String,
        case_title: String, case_detail: String,
        opponent_party_lawyer_name: String
    )
            : Response<AddCaseResponse>
    suspend fun add_hearingApi(
        token: String,
        client_id: RequestBody,
        case_id: RequestBody,
        case_stage: RequestBody,
        case_status: RequestBody,
        current_date: RequestBody,
        hearing_details: RequestBody,
        hearing_date: RequestBody,
        hearing_doc: MultipartBody.Part
    ): Response<AddHearingResponse>

    suspend fun add_caseApi(
        token: String, start_date: String, first_hearing_date: String,
        opponent_party_name: String, case_stage: String,
        case_status: String, judge_name: String, cnr_number: String, court_name: String,
        case_title: String, case_detail: String,
        client_id: String, opponent_party_lawyer_name: String
    )
            : Response<AddCaseResponse>
    suspend fun client_search(
        token: String,
        search: String,
    )
            : Response<SearchResponseListModelClass>
}


