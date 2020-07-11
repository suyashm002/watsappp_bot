package com.autoai.readnotification.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.autoai.readnotification.MyNotifiService
import com.autoai.readnotification.R
import com.autoai.readnotification.ui.CustomizeMessageActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    var bottomNavigation: BottomNavigationView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // val button2 = findViewById<Button>(R.id.button2)
        val intent = Intent(this@MainActivity, MyNotifiService::class.java)
        startService(intent) //Start service
        val sp = getSharedPreferences("msg", Context.MODE_PRIVATE)
      /*  button2.setOnClickListener { //Open listener reference message // Notification access
            val intent_s = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent_s)
        }*/
        if (isNotificationServiceRunning) {
            val intent1 = Intent(baseContext, CustomizeMessageActivity::class.java)
            startActivity(intent1)
        }
    }

    override fun onStart() {
        super.onStart()
    }

    fun openNotificationSettings(view: View?) {
        startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
    }

    private val isNotificationServiceRunning: Boolean
        private get() {
            val contentResolver = contentResolver
            val enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            val packageName = packageName
            return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (isNotificationServiceRunning) {
            val intent = Intent(baseContext, CustomizeMessageActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        // EditText editText;
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }
}