package com.tuppersoft.trigger.features.service

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.os.PowerManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.tuppersoft.skizo.android.core.extension.logd
import com.tuppersoft.trigger.R
import com.tuppersoft.trigger.core.extension.canWrite
import com.tuppersoft.trigger.features.broadcast.BluetoothReceiver
import com.tuppersoft.trigger.features.main.MainActivity
import java.util.Locale

class MonitorService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    private val bluetoothReceiver = BluetoothReceiver()

    override fun onBind(intent: Intent): IBinder? {
        "Some component want to bind with the service".logd()
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        ("onStartCommand executed with startId: $startId").logd()
        if (intent != null) {
            val action = intent.action
            ("using an intent with action $action").logd()
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                Actions.TURN_ON.name -> turnOn()
                Actions.TURN_OFF.name -> turnOff()
                else -> ("This should never happen. No action in the received intent").logd()
            }
        } else {
            "with a null intent. It has been probably restarted by the system.".logd()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        "The service has been created".toUpperCase(Locale.ROOT).logd()
        val notification = createNotification()
        startForeground(0x111, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        "The service has been destroyed".toUpperCase(Locale.ROOT).logd()
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun startService() {
        if (isServiceStarted) return
        ("Starting the foreground service task").logd()
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }

        startBroadcast()
    }

    private fun stopService() {
        "Stopping the foreground service".logd()
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            "Service stopped without being started: ${e.message}".logd()
        }
        isServiceStarted = false
        turnOff()
        stopBroadcast()
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            val channel = NotificationChannel(
                notificationChannelId,
                "Service notifications channel",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Service channel"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("Service is running")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_service)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_MIN)
            .build()
    }

    private fun startBroadcast() {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        }

        try {
            applicationContext.registerReceiver(bluetoothReceiver, filter)
            "Start bluetoothReceiver".logd()
        } catch (e: java.lang.Exception) {
            "bluetoothReceiver was registred".logd()
        }
    }

    private fun stopBroadcast() {

        try {
            applicationContext.unregisterReceiver(bluetoothReceiver)
            "Stop bluetoothReceiver".logd()
        } catch (e: java.lang.Exception) {
            "bluetoothReceiver wasn't registred".logd()
        }
    }

    private fun turnOn() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && canWrite()
        ) {
            MyOreoWifiManager(applicationContext).apply {

                startTethering(object : MyOnStartTetheringCallback() {
                    override fun onTetheringStarted() {}

                    override fun onTetheringFailed() {}
                })
            }
        } else {
            "No permission".logd()
            stopService()
        }
    }

    private fun turnOff() {
        MyOreoWifiManager(applicationContext).stopTethering()
    }
}
