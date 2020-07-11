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
import com.autoai.readnotification.ui.fragments.AboutPopupFragment
import com.autoai.readnotification.ui.fragments.CustomizeMessageFragment
import com.autoai.readnotification.ui.fragments.NotificationListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = CustomizeMessageFragment()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
        }

        val intent = Intent(this@MainActivity, MyNotifiService::class.java)
        startService(intent) //Start service
        val sp = getSharedPreferences("msg", Context.MODE_PRIVATE)
      /*  button2.setOnClickListener { //Open listener reference message // Notification access
            val intent_s = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent_s)
        }*/
        if (isNotificationServiceRunning) {
           // val intent1 = Intent(baseContext, CustomizeMessageActivity::class.java)
           // startActivity(intent1)
        }

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.navigation_home -> {
                val fragment = CustomizeMessageFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sms -> {
                val fragment = NotificationListFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                val fragment = AboutPopupFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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
          //  val intent = Intent(baseContext, CustomizeMessageActivity::class.java)
           // startActivity(intent)
        }
    }

    companion object {
        // EditText editText;
        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
    }
}