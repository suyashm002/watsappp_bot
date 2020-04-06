package com.suyash.autoreply.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.suyash.autoreply.R
import com.suyash.autoreply.models.SaveCustomeMessage
import io.realm.Realm
import kotlinx.android.synthetic.main.create_update_activity.*

class CreateUpdateActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.create_update_activity)

        realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        realm.commitTransaction()

        done_button.setOnClickListener {View ->
            initEditText()
        }
        close_button.setOnClickListener {
            finish()
        }
    }
    private fun initEditText() {
        realm.executeTransactionAsync({
            val message = it.createObject(SaveCustomeMessage::class.java)
            message.expectedMessage = message_received_edittext.text.toString()
            message.replyMessage = message_reply_edittext.text.toString()

        }, {
            Log.d("Save Success", "On Success: Data Written Successfully!")
            finish()
            clearEditText()
        }, {
            Log.d("Save Success", "On Error: Error in saving Data!")
        })

    }
    private fun clearEditText() {
        message_received_edittext.setText("")
        message_reply_edittext.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}