package com.suyash.autoreply.ui

import android.content.Intent
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
    private var isUpdateMessage = false
    private var messageId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.create_update_activity)

        isUpdateMessage = intent.getBooleanExtra(CustomizeMessageActivity.IS_UPDATE_MESSAGE, false)
        if (intent != null && intent.getParcelableExtra<SaveCustomeMessage>("DATA") != null && isUpdateMessage) {
            updateMessage(intent.getParcelableExtra("DATA"))
            delete_button.visibility = View.VISIBLE

            messageId = intent.getParcelableExtra<SaveCustomeMessage>("DATA").expectedMessage.toString()
        } else {
            delete_button.visibility = View.GONE
        }
        realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        realm.commitTransaction()

        done_button.setOnClickListener { View ->
            if (checkError()) {
                initEditText()
            } else {
                showError()
            }
        }
        delete_button.setOnClickListener {
            deleteMessage()
        }
        close_button.setOnClickListener {
            finish()
        }
    }

    private fun showError() {
        message_received.isErrorEnabled = true
        message_reply.isErrorEnabled = true
    }

    private fun checkError(): Boolean {
        return !message_received_edittext.text?.isEmpty()!! && !message_reply_edittext.text?.isEmpty()!!
    }

    private fun deleteMessage() {
        val msgs = realm
                .where(SaveCustomeMessage::class.java)
                .findAll()

        val userdatabase = msgs
                .where()
                .equalTo("expectedMessage", message_received_edittext.text.toString())
                .equalTo("replyMessage", message_reply_edittext.text.toString())
                .findFirst()

        if (userdatabase != null) {

            if (!realm.isInTransaction) {
                realm.beginTransaction()
            }

            userdatabase.deleteFromRealm()
            realm.commitTransaction()
        }
        finish()
    }

    private fun updateMessage(saveCustomeMessage: SaveCustomeMessage) {
        message_received_edittext.setText(saveCustomeMessage.expectedMessage)
        message_reply_edittext.setText(saveCustomeMessage.replyMessage)
    }

    private fun initEditText() {
        if (isUpdateMessage) {
            header.setText(getString(R.string.update))
            realm.executeTransaction {
                var saveCustomeMessage = it.where(SaveCustomeMessage::class.java).equalTo("expectedMessage", messageId).findFirst()
                saveCustomeMessage.expectedMessage = message_received_edittext.text?.toString()
                saveCustomeMessage.replyMessage = message_reply_edittext.text?.toString()
            }
            finish()

        } else {
            header.setText(getString(R.string.create))
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