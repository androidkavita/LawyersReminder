package com.lawyersbuddy.app.prefs

import android.content.Context
import android.content.SharedPreferences
import com.lawyersbuddy.app.model.LoginData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPref @Inject constructor(@ApplicationContext context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()
    var user: LoginData
        get() = gson.fromJson<Any>(preferences.getString("user", null), LoginData::class.java) as LoginData
        set(user) {
            val gson = Gson()
            val loginRes = gson.toJson(user)
            preferences.edit().putString("user", loginRes).apply()
        }
    var isLogin: Boolean
        get() = preferences.getBoolean("isLoginA", false)
        set(login) = preferences.edit().putBoolean("isLoginA", login).apply()

    fun clearPref() {
        preferences.edit().clear().apply()
    }


    fun getLocale(): String {
        return preferences.getString("locale", "en")!!
    }

    fun setLocale(locale: String) {
        preferences.edit().putString("locale", locale).apply()
    }



    fun setName(name: String) {
        preferences.edit().putString("name", name).apply()
    }

    fun getName(): String? {
        return preferences.getString("name", null)
    }

    fun setToken(token: String?) {
        preferences.edit().putString("token", token).apply()
    }
    fun getToken(): String? {
        return preferences.getString("token", null)
    }
    fun setnoverification(verification: String?) {
        preferences.edit().putString("verification", verification).apply()
    }
    fun getnoverification(): String? {
        return preferences.getString("verification", null)
    }
    fun setNotificationType(type: String?) {
        preferences.edit().putString("notification_type", type).apply()
    }
    fun getNotificationType(): String? {
        return preferences.getString("notification_type", null)
    }

    fun setEmail(email: String?) {
        preferences.edit().putString("email", email).apply()
    }

    fun getEmail(): String? {
        return preferences.getString("email", null)
    }

    fun setMobile(mobile: String?) {
        preferences.edit().putString("mobile", mobile).apply()
    }

    fun getmobile(): String? {
        return preferences.getString("mobile", null)
    }




    fun getAddress(): String? {
        return preferences.getString("address", null)
    }

    fun setAddress(address: String?) {
        preferences.edit().putString("address", address).apply()
    }

    fun getUserProfileImage(): String? {
        return preferences.getString("profile_image", null)
    }

    fun setProfileImage(profile_image: String?) {
        preferences.edit().putString("profile_image", profile_image).apply()
    }

    fun getBarAssociationNumber(): String? {
        return preferences.getString("barAssociationNumber", null)
    }
    fun setBarAssociationNumber(barAssociationNumber: String?) {
        preferences.edit().putString("barAssociationNumber", barAssociationNumber).apply()
    }
    fun getBarCouncilNumber(): String? {
        return preferences.getString("barCouncilNumber", null)
    }
    fun setBarCouncilNumber(barCouncilNumber: String?) {
        preferences.edit().putString("barCouncilNumber", barCouncilNumber).apply()
    }






















    fun setid(token: String?) {
        preferences.edit().putString("id", token).apply()
    }

    fun getid(): String? {
        return preferences.getString("id", null)
    }

    fun setusertype1(user_type:String?) {
        preferences.edit().putString("role",user_type.toString()).apply()
    }

    //for FCM Token
    fun getFcmToken(): String? {
        return preferences.getString("fcmtoken", null)
    }

    fun setFcmToken(token: String?) {
        preferences.edit().putString("fcmtoken", token).apply()
    }


    fun getCity(): String? {
        return preferences.getString("city", null)
    }

    fun setCity(city: String?) {
        preferences.edit().putString("city", city).apply()
    }
    fun getuid(): String? {
        return preferences.getString("uid", null)
    }

    fun setuid(city: String?) {
        preferences.edit().putString("uid", city).apply()
    }
    fun getType(): String? {
        return preferences.getString("type", null)
    }

    fun setType(type: String?) {
        preferences.edit().putString("type", type).apply()
    }

    fun getState(): String? {
        return preferences.getString("state", null)
    }

    fun setState(state: String?) {
        preferences.edit().putString("state", state).apply()
    }




    fun getCityId(): String? {
        return preferences.getString("cityId", null)
    }

    fun setCityId(cityId: String?) {
        preferences.edit().putString("cityId", cityId).apply()
    }
    fun getPermission(): String? {
        return preferences.getString("is_permission", null)
    }

    fun setPermission(is_permission: String?) {
        preferences.edit().putString("is_permission", is_permission).apply()
    }

    fun getStateId(): String? {
        return preferences.getString("stateId", null)
    }

    fun setStateId(stateId: String?) {
        preferences.edit().putString("stateId", stateId).apply()
    }











    fun getPinCode(): String? {
        return preferences.getString("pincode", null)
    }

    fun setpinCode(pincode: String?) {
        preferences.edit().putString("pincode", pincode).apply()
    }

    fun getSubtitles(): Boolean {
        return preferences.getBoolean("isSubtitles", false)
    }

    fun setSubtitles(isSubtitles: Boolean) {
        preferences.edit().putBoolean("isSubtitles", isSubtitles).apply()
    }

    fun getDownloadWifi(): Boolean {
        return preferences.getBoolean("isDownloadWifi", false)
    }

    fun setDownloadWifi(isDownloadWifi: Boolean) {
        preferences.edit().putBoolean("isDownloadWifi", isDownloadWifi).apply()
    }

    fun getNotification(): Boolean {
        return preferences.getBoolean("isNotification", true)
    }

    fun setNotification(isNotification: Boolean) {
        preferences.edit().putBoolean("isNotification", isNotification).apply()
    }

    fun getPreferredLanguage(): String {
        return preferences.getString("preferredLanguage", "English")!!
    }

    fun setPreferredLanguage(preferredLanguage: String) {
        preferences.edit().putString("preferredLanguage", preferredLanguage).apply()
    }

    fun getLoginType(): String {
        return preferences.getString("loginType", "1")!!
    }

    fun setLoginType(loginType: String) {
        preferences.edit().putString("loginType", loginType).apply()
    }


}