package com.lawyersbuddy.app.data

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.lawyersbuddy.app.R
import com.lawyersbuddy.app.activities.MainActivity
import com.lawyersbuddy.app.activities.SplashScreen
import com.lawyersbuddy.app.model.FirebasePushResponse
import com.lawyersbuddy.app.prefs.UserPref
import org.json.JSONObject
import kotlin.random.Random

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseMessagingServices : FirebaseMessagingService() {
    lateinit var userPref: UserPref
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        userPref = UserPref(this)
        Log.d("notificationData", remoteMessage.data.toString())
        if (remoteMessage.data.isNotEmpty()) {
            val gSon = Gson()
            val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
            val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                jsonObject.toString(),
                FirebasePushResponse::class.java
            )
            //wakeUpScreen()
            sendNotification(firebasePushResponse)
        } else {
            val gSon = Gson()
            val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
            val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                jsonObject.toString(),
                FirebasePushResponse::class.java
            )
            firebasePushResponse.title =
                remoteMessage.notification!!.title!!

            firebasePushResponse.body = remoteMessage.notification!!.body!!

            // sendNotification(firebasePushResponse);
            sendNotification(firebasePushResponse)
        }

    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    var NOTIFICATION_ID: Int = 234

    private fun sendNotification(messageBody: FirebasePushResponse) {
        val CHANNEL_ID = "my_channel_01"
        val intent: Intent

        intent = Intent(this, SplashScreen::class.java)
        intent.putExtra("type", 1)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.splash_screen_logo)
            .setContentTitle(messageBody.title)
            .setContentText(messageBody.body)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    messageBody.body
                )
            )
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(rand(1, 100), notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    fun rand(from: Int, to: Int): Int {
        val random = java.util.Random()
        return random.nextInt(to - from) + from
    }

}