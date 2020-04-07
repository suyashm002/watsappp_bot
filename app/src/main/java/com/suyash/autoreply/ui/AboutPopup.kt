package com.suyash.autoreply.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.suyash.autoreply.R
import kotlinx.android.synthetic.main.about_popup.*

class AboutPopup(context: Context) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         val view: View =
                LayoutInflater.from(context).inflate(R.layout.about_popup, container, false)

        close_button.setOnClickListener { dismiss() }
        return view
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }






}