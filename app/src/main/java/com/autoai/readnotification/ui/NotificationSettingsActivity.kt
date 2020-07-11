package com.autoai.readnotification.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.autoai.readnotification.R
import kotlinx.android.synthetic.main.notification_settings.*

class NotificationSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_settings)


        close_button.setOnClickListener {
            finish()
        }

    }
}