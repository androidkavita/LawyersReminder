package com.lawyersbuddy.app.data


import com.lawyersbuddy.app.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @FormUrlEncoded
    @POST("signup")
    suspend fun signup(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("mobile") mobile: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("bar_association") bar_association: String,
        @Field("bar_council_number") bar_council_number: String,
        @Field("termcondition") termcondition: String,
        @Field("firm_name") firm_name: String,
        @Field("address") address: String
    ): Response<SignupResponse>


    @Multipart
    @POST("add_client")
    suspend fun add_client(
        @Header("Authorization") authorization: String,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<AddClientResponse>

    @GET("payment_list")
    suspend fun payment_list(
        @Header("Authorization") authorization: String
    ): Response<PaymentListResponse>

    @GET("notification_status")
    suspend fun notificationStatusApi(
        @Header("Authorization") authorization: String
    ): Response<LoginResponse>

    @GET("state_list")
    suspend fun state_list(): Response<StateListResponse>

    @FormUrlEncoded
    @POST("client_list")
    suspend fun client_list(
        @Header("Authorization") authorization: String,
        @Field("search") search: String
    ): Response<ClientListResponse>

    @GET("home")
    suspend fun home(@Header("Authorization") authorization: String): Response<HomeResponse>

    @FormUrlEncoded
    @POST("case_list")
    suspend fun case_list(
        @Header("Authorization") authorization: String,
        @Field("search") search: String,
        @Field("searh_date") searh_date: String
    ): Response<CasesResponse>

    @GET("notification_list")
    suspend fun notification_list(@Header("Authorization") authorization: String): Response<NotificationResponse>

    @GET("ContactUS")
    suspend fun ContactUS(): Response<ContactUsResponse>

    @GET("withus")
    suspend fun withus(): Response<AboutUsResponse>

    @GET("aboutus")
    suspend fun aboutus(): Response<AboutUsResponse>

    @GET("term_condition")
    suspend fun term_condition(): Response<AboutUsResponse>


    @FormUrlEncoded
    @POST("assistant_list")
    suspend fun assistant_list(
        @Header("Authorization") authorization: String,
        @Field("search") search: String
    ): Response<AssistantListResponse>

    @FormUrlEncoded
    @POST("assistant_detail")
    suspend fun assistantDetail(
        @Header("Authorization") authorization: String,
        @Field("assistant_id") assistant_id: String
    ): Response<AddAssistantResponse>

    @FormUrlEncoded
    @POST("client_detail")
    suspend fun client_detail(
        @Header("Authorization") authorization: String,
        @Field("client_id") client_id: String
    ): Response<ClientDetailsResponse>

    @FormUrlEncoded
    @POST("case_detail")
    suspend fun case_detail(
        @Header("Authorization") authorization: String,
        @Field("case_id") case_id: String
    ): Response<CaseDetailsResponse>

    @FormUrlEncoded
    @POST("city_list")
    suspend fun city_list(@Field("state_id") state_id: String): Response<CityListResponse>

    @FormUrlEncoded
    @POST("add_contact")
    suspend fun add_contact(
        @Field("message") message: String,
        @Field("name") name: String,
        @Field("email") email: String,
    ): Response<ContactUsResponse>

    @FormUrlEncoded
    @POST("send_otp")
    suspend fun send_otp(@Field("mobile") mobile: String): Response<OtpResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("type") type: String,
        @Field("device_token") device_token: String
    ): Response<LoginResponse>

   @FormUrlEncoded
    @POST("google_login")
    suspend fun google_login(
       @Field("type") type: String,
       @Field("email") email: String,
        @Field("google_id") google_id: String,
        @Field("name") name: String,
        @Field("device_id") device_id: String,
        @Field("device_token") device_token: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("forgot_password")
    suspend fun forgot_password(
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("delete_user")
    suspend fun delete_user(
        @Header("Authorization") authorization: String,
        @Field("id") id: String,
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("remove_hearing_img")
    suspend fun remove_hearing_img(
        @Header("Authorization") authorization: String,
        @Field("file_name") file_name: String,
        @Field("hearing_id") hearing_id: String,
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("user_detail")
    suspend fun user_detail(
        @Field("email") email: String,
    ): Response<LoginResponse>

  @FormUrlEncoded
    @POST("payment")
    suspend fun payment(
        @Header("Authorization") authorization: String,
        @Field("subscription_id") subscription_id: String,
        @Field("transaction_id") transaction_id: String,
        @Field("amount") amount: String,
        @Field("validity") validity: String,
        @Field("payment_created") payment_created: String,
    ): Response<LoginResponse>

    @Multipart
    @POST("add_assistant")
    suspend fun add_assistant(
        @Header("Authorization") authorization: String,
        @Part("mobile") mobile: RequestBody,
        @Part("password") address: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("is_permission") is_permission: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<AddAssistantResponse>

    @Multipart
    @POST("edit_assistant")
    suspend fun editAssistant(
        @Header("Authorization") authorization: String,
        @Part("assistant_id") assistant_id: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("password") address: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("is_permission") is_permission: RequestBody,
        @Part image: MultipartBody.Part?

    ): Response<AddAssistantResponse>

    @FormUrlEncoded
    @POST("change_password")
    suspend fun changePasswordApi(
        @Header("Authorization") authorization: String,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String
    ): Response<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("download_all_events")
    suspend fun download_all_events(
        @Header("Authorization") authorization: String,
        @Field("date") date: String,
        @Field("lawyer_id") lawyer_id: String
    ): Response<DownloadTodayEventResponse>

    @FormUrlEncoded
    @POST("notification_on_off")
    suspend fun notification_on_off(
        @Header("Authorization") authorization: String,
        @Field("noti_status") type: String,
        @Field("user_id") user_id: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("home_search")
    suspend fun home_search(
        @Header("Authorization") authorization: String,
        @Field("search_name") search_name: String,
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String,
        @Field("search_type") search_type: String
    ): Response<HomeSearchResponse>

    @FormUrlEncoded
    @POST("calendar_list")
    suspend fun calendar_list(
        @Header("Authorization") authorization: String,
        @Field("date") date: String
    ): Response<CalenderListResponse>

    @GET("get_profile")
    suspend fun get_profileApi(@Header("Authorization") authorization: String): Response<GetProfileResponseModel>

    @GET("coupon_list")
    suspend fun coupon_listApi(@Header("Authorization") authorization: String): Response<OffersResponseModel>

    @GET("download_case_detail")
    suspend fun downloadCaseDetailPdfApi(
        @Header("Authorization") authorization: String,
        @Query("case_id") case_id: String
    ): Response<CaseDetailsResponse>

    @GET("download_hearing_detail")
    suspend fun downloadHearingDetailPdfApi(
        @Header("Authorization") authorization: String,
        @Query("hearing_id") case_id: String
    ): Response<DownloadPdfResponse>

    @Multipart
    @POST("edit_profile")
    suspend fun editProfileApi(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("bar_association") bar_association: RequestBody,
        @Part("bar_council_number") bar_council_number: RequestBody,
        @Part("firm_name") firm_name: RequestBody,
        @Part("zip_code") zip_code: RequestBody,
        @Part("address") address: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<LoginResponse>

    @Multipart
    @POST("edit_hearing")
    suspend fun edit_hearingApi(
        @Header("Authorization") authorization: String,
        @Part("hearing_id") hearing_id: RequestBody,
        @Part("case_stage") case_stage: RequestBody,
        @Part("case_status") case_status: RequestBody,
        @Part("current_date") current_date: RequestBody,
        @Part("hearing_details") hearing_details: RequestBody,
        @Part("hearing_date") hearing_date: RequestBody,
        @Part("final_date") final_date: RequestBody,
        @Part("is_auto_reminder") is_auto_reminder: RequestBody,
        @Part("doc_type") doc_type: RequestBody,
        @Part hearing_doc: ArrayList<MultipartBody.Part?>
    ): Response<EditHearingResponse>

    @FormUrlEncoded
    @POST("hearing_list")
    suspend fun hearing_listApi(
        @Header("Authorization") authorization: String,
        @Field("search") search: String,
        @Field("searchdate") searchdate: String
    ): Response<HearingListResponse>

    @FormUrlEncoded
    @POST("hearing_detail")
    suspend fun hearing_detailApi(
        @Header("Authorization") authorization: String,
        @Field("hearing_id") client_id: String
    ): Response<HearingDetailsResponse>

    @FormUrlEncoded
    @POST("client_search")
    suspend fun client_search(
        @Header("Authorization") authorization: String,
        @Field("search") client_id: String
    ): Response<SearchResponseListModelClass>

    @FormUrlEncoded
    @POST("add_hearing")
    suspend fun add_hearingApi(
        @Header("Authorization") authorization: String,
        @Field("client_id") client_id: RequestBody,
        @Field("case_id") case_id: RequestBody,
        @Field("case_stage") case_stage: RequestBody,
        @Field("case_status") case_status: RequestBody,
        @Field("current_date") current_date: RequestBody,
        @Field("hearing_details") hearing_details: RequestBody,
        @Field("hearing_date") hearing_date: RequestBody,
        @Part hearing_doc: MultipartBody.Part?
    ): Response<AddHearingResponse>

    @FormUrlEncoded
    @POST("add_case")
    suspend fun add_caseApi(
        @Header("Authorization") authorization: String,
        @Field("start_date") start_date: String,
        @Field("first_hearing_date") first_hearing_date: String,
        @Field("opponent_party_name") opponent_party_name: String,
        @Field("case_stage") case_stage: String,
        @Field("case_status") case_status: String,
        @Field("judge_name") judge_name: String,
        @Field("cnr_number") cnr_number: String,
        @Field("court_name") court_name: String,
        @Field("case_title") case_title: String,
        @Field("case_detail") case_detail: String,
        @Field("client_id") client_id: String,
        @Field("opponent_party_lawyer_name") opponent_party_lawyer_name: String
    ): Response<AddCaseResponse>

    @FormUrlEncoded
    @POST("edit_case")
    suspend fun edit_case(
        @Header("Authorization") authorization: String,
        @Field("case_id") case_id: String,
        @Field("start_date") start_date: String,
        @Field("opponent_party_name") opponent_party_name: String,
        @Field("case_stage") case_stage: String,
        @Field("case_status") case_status: String,
        @Field("judge_name") judge_name: String,
        @Field("cnr_number") cnr_number: String,
        @Field("court_name") court_name: String,
        @Field("case_title") case_title: String,
        @Field("case_detail") case_detail: String,
        @Field("opponent_party_lawyer_name") opponent_party_lawyer_name: String
    ): Response<AddCaseResponse>

    @Multipart
    @POST("edit_client")
    suspend fun editClientApi(
        @Header("Authorization") authorization: String,
        @Part("client_id") client_id: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("address") address: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ClientDetailsResponse>

    @GET("subscription")
    suspend fun subscriptionListApi(@Header("Authorization") authorization: String): Response<SubscriptionPlanResponse>

    @FormUrlEncoded
    @POST("subscription_detaiil")
    suspend fun subscriptionDetailApi(
        @Header("Authorization") authorization: String,
        @Field("subscription_id") subscription_id: String
    ): Response<SubscriptionDetailResponse>

}
