package com.lawyersbuddy.app.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.lawyersbuddy.app.R
import okhttp3.Interceptor
import okhttp3.Response
import java.net.SocketTimeoutException

class NetworkConnectionInterceptor(
    val context: Context
) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoInternetException(context.getString(R.string.make_sure_you_have_an_active_data_connection))
        } else {
            try {
                return chain.proceed(chain.request())
            } catch (exception: SocketTimeoutException) {
                throw SocketTimeoutException(context.getString(R.string.connection_timeout))
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
        return throw ApiException(context.getString(R.string.oops_somethings_went_wrong))
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

                    it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            else -> false
                        }
                    }
            }
        }
        return result
    }
}