package com.lawyersbuddy.app.data


import com.lawyersbuddy.app.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val apiService: ApiService) : MainRepository {

    override suspend fun signup(
        name: String, email: String, password: String, mobile: String,
        city: String, state: String, bar_association: String,
        bar_council_number: String, termcondition: String,
        firm_name: String, address: String
    ):
            Response<SignupResponse> = apiService.signup(
        name, email, password, mobile, city, state,
        bar_association, bar_council_number, termcondition, firm_name, address
    )


    override suspend fun add_client(
        token: String,
        mobile: RequestBody,
        address: RequestBody,
        name: RequestBody,
        email: RequestBody,
        image: MultipartBody.Part
    ):

            Response<AddClientResponse> =
        apiService.add_client(token, mobile, address, name, email, image)

    override suspend fun editClient(
        token: String,
        client_id: RequestBody,
        mobile: RequestBody,
        address: RequestBody,
        name: RequestBody,
        email: RequestBody,
        image: MultipartBody.Part
    ):

            Response<ClientDetailsResponse> =
        apiService.editClientApi(token, client_id, mobile, address, name, email, image)

    override suspend fun add_assistant(
        token: String,
        mobile: RequestBody,
        password: RequestBody,
        name: RequestBody,
        email: RequestBody,
        is_permission: RequestBody,
        image: MultipartBody.Part
    ):

            Response<AddAssistantResponse> =
        apiService.add_assistant(token, mobile, password, name, email, is_permission, image)

    override suspend fun editAssistant(
        token: String,
        assistant_id: RequestBody,
        mobile: RequestBody,
        password: RequestBody,
        name: RequestBody,
        email: RequestBody,
        is_permission: RequestBody, image: MultipartBody.Part
    ):

            Response<AddAssistantResponse> =
        apiService.editAssistant(
            token,
            assistant_id,
            mobile,
            password,
            name,
            email,
            is_permission,
            image
        )


    override suspend fun state_list(): Response<StateListResponse> = apiService.state_list()
    override suspend fun client_list(token: String, search: String): Response<ClientListResponse> =
        apiService.client_list(token, search)

    override suspend fun notification_list(token: String): Response<NotificationResponse> =
        apiService.notification_list(token)

    override suspend fun home(token: String): Response<HomeResponse> = apiService.home(token)
    override suspend fun case_list(
        token: String,
        search: String,
        searh_date: String
    ): Response<CasesResponse> =
        apiService.case_list(token, search, searh_date)

    override suspend fun ContactUS(): Response<ContactUsResponse> = apiService.ContactUS()
    override suspend fun aboutus(): Response<AboutUsResponse> = apiService.aboutus()
    override suspend fun term_condition(): Response<AboutUsResponse> = apiService.term_condition()
    override suspend fun withus(): Response<AboutUsResponse> = apiService.withus()
    override suspend fun assistant_list(
        token: String,
        search: String
    ): Response<AssistantListResponse> =
        apiService.assistant_list(token, search)

    override suspend fun client_detail(
        token: String,
        client_id: String
    ): Response<ClientDetailsResponse> = apiService.client_detail(token, client_id)

    override suspend fun assistantDetailApi(
        token: String,
        assistant_id: String
    ): Response<AddAssistantResponse> = apiService.assistantDetail(token, assistant_id)

    override suspend fun case_detail(
        token: String,
        case_id: String
    ): Response<CaseDetailsResponse> = apiService.case_detail(token, case_id)

    override suspend fun city_list(state_id: String): Response<CityListResponse> =
        apiService.city_list(state_id)

    override suspend fun send_otp(mobile: String): Response<OtpResponse> =
        apiService.send_otp(mobile)

    override suspend fun login(
        email: String,
        password: String,
        type: String,token:String
    ): Response<LoginResponse> = apiService.login(email, password, type,token)

    override suspend fun add_contact(

        message: String,
        name: String,
        email: String
    ): Response<ContactUsResponse> = apiService.add_contact(message,name,email)


    override suspend fun google_login(
        type: String,
        email: String,
        google_id: String,
        name: String,
        device_id: String,
        device_token: String,

        ): Response<LoginResponse> = apiService.google_login(
        type,
        email,
        google_id,
        name,
        device_id,
        device_token
    )

    override suspend fun calendar_list(
        token: String,
        date: String
    ): Response<CalenderListResponse> = apiService.calendar_list(token, date)

    override suspend fun forgot_password(
        mobile: String,
        password: String
    ): Response<LoginResponse> = apiService.forgot_password(mobile, password)

    override suspend fun delete_user(
        token: String,
        id: String,
    ): Response<LoginResponse> = apiService.delete_user(token, id)
    override suspend fun remove_hearing_img(
        token: String,
        file_name: String,
        hearing_id: String,
    ): Response<LoginResponse> = apiService.remove_hearing_img(token,file_name, hearing_id)

    override suspend fun user_detail(
        email: String,

        ): Response<LoginResponse> = apiService.user_detail(email)


    override suspend fun payment(
        token: String,
        subscription_id: String,
        transaction_id: String,
        amount: String,
        validity: String,
        payment_created: String

    ): Response<LoginResponse> = apiService.payment(
        token,
        subscription_id,
        transaction_id,
        amount,
        validity,
        payment_created
    )


    override suspend fun payment_list(header: String): Response<PaymentListResponse> {
        return apiService.payment_list(header)
    }
    override suspend fun notification_on_off_list(header: String): Response<LoginResponse> {
        return apiService.notificationStatusApi(header)
    }

    override suspend fun changePasswordApi(
        token: String,
        old_password: String,
        new_password: String
    ): Response<ChangePasswordResponse> =
        apiService.changePasswordApi(token, old_password, new_password)

    override suspend fun download_all_events(
        token: String,
        date: String,
       lawyer_id: String
    ): Response<DownloadTodayEventResponse> =
        apiService.download_all_events(token, date, lawyer_id)
override suspend fun notification_on_off(
    token: String,
     type: String,
    user_id: String
    ): Response<LoginResponse> =
        apiService.notification_on_off(token, type, user_id)

    override suspend fun home_searchApi(
        token: String,
        search_name: String,
        start_date: String,
        end_date: String,
        search_type: String
    ): Response<HomeSearchResponse> =
        apiService.home_search(token, search_name, start_date, end_date, search_type)

    override suspend fun get_profileApi(token: String): Response<GetProfileResponseModel> =
        apiService.get_profileApi(token)

    override suspend fun coupon_listApi(token: String): Response<OffersResponseModel> =
        apiService.coupon_listApi(token)

    override suspend fun subscriptionListApi(token: String): Response<SubscriptionPlanResponse> =
        apiService.subscriptionListApi(token)

    override suspend fun subscriptionDetailApi(
        token: String,
        subscription_id: String
    ): Response<SubscriptionDetailResponse> =
        apiService.subscriptionDetailApi(token, subscription_id)

    override suspend fun downloadCaseDetailApi(
        token: String,
        case_id: String
    ): Response<CaseDetailsResponse> =
        apiService.downloadCaseDetailPdfApi(token, case_id)

    override suspend fun downloadHearingDetailApi(
        token: String,
        hearing_id: String
    ): Response<DownloadPdfResponse> =
        apiService.downloadHearingDetailPdfApi(token, hearing_id)

    override suspend fun editProfileApi(
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
    ): Response<LoginResponse> = apiService.editProfileApi(
        token,
        name,
        state,
        city,
        bar_association,
        bar_council_number,
        firm_name,
        zip_code,
        address,
        image
    )

    override suspend fun edit_hearingApi(
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
        hearing_doc: ArrayList<MultipartBody.Part?>
    ): Response<EditHearingResponse> =
        apiService.edit_hearingApi(
            token, hearing_id, case_stage, case_status,
            current_date, hearing_details, hearing_date, final_date, is_auto_reminder, doc_type,hearing_doc
        )

    override suspend fun hearing_listApi(
        token: String,
        search: String,
        searh_date: String
    ): Response<HearingListResponse> =
        apiService.hearing_listApi(token, search, searh_date)

    override suspend fun hearing_detailApi(
        token: String,
        hearing_id: String
    ): Response<HearingDetailsResponse> = apiService.hearing_detailApi(token, hearing_id)

    override suspend fun edit_case(
        token: String, case_id: String, start_date: String,
        opponent_party_name: String, case_stage: String,
        case_status: String, judge_name: String,
        cnr_number: String, court_name: String, case_title: String,
        case_detail: String, opponent_party_lawyer_name: String
    ):

            Response<AddCaseResponse> = apiService.edit_case(
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

    override suspend fun add_hearingApi(
        token: String,
        client_id: RequestBody,
        case_id: RequestBody,
        case_stage: RequestBody,
        case_status: RequestBody,
        current_date: RequestBody,
        hearing_details: RequestBody,
        hearing_date: RequestBody,
        hearing_doc: MultipartBody.Part
    ): Response<AddHearingResponse> = apiService.add_hearingApi(
        token,
        client_id,
        case_id,
        case_stage,
        case_status,
        current_date,
        hearing_details,
        hearing_date,
        hearing_doc
    )

    override suspend fun client_search(
        token: String,
        search: String
    )
            : Response<SearchResponseListModelClass> = apiService.client_search(token, search)

    override suspend fun add_caseApi(
        token: String, start_date: String, first_hearing_date: String,
        opponent_party_name: String, case_stage: String,
        case_status: String, judge_name: String,
        cnr_number: String, court_name: String, case_title: String,
        case_detail: String, client_id: String, opponent_party_lawyer_name: String
    ):

            Response<AddCaseResponse> = apiService.add_caseApi(
        token,
        start_date,
        first_hearing_date,
        opponent_party_name,
        case_stage,
        case_status,
        judge_name,
        cnr_number,
        court_name,
        case_title,
        case_detail,
        client_id,
        opponent_party_lawyer_name
    )


}