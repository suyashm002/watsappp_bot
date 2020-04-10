package com.suyash.autoreply.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.suyash.autoreply.BuildConfig
import com.suyash.autoreply.R
import kotlinx.android.synthetic.main.about_popup.*


class AboutPopup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_popup)
        close_dialog_button.setOnClickListener { finish() }

        share_app.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

        github_link.setOnClickListener {
            val openURL = Intent(android.content.Intent.ACTION_VIEW)
            openURL.data = Uri.parse("https://github.com/suyashm002/watsappp_bot/")
            startActivity(openURL)
        }

    }




}