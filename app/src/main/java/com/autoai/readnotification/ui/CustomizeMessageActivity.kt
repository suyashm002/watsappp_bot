package com.autoai.readnotification.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.autoai.readnotification.R

import com.autoai.readnotification.models.SaveCustomeMessage
import io.realm.Realm
import kotlinx.android.synthetic.main.customize_message_activity.*

import com.autoai.readnotification.ReplyMessageAdapter
import java.util.*
import kotlin.collections.ArrayList




class CustomizeMessageActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    lateinit var list: ArrayList<SaveCustomeMessage>
    // lateinit var adapter: ReplyMessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customize_message_activity)

        realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        realm.commitTransaction()



        recyclerview.layoutManager = LinearLayoutManager(this)

        iniateAdapter()
        submit_button.setOnClickListener { View -> initEditText() }
    }

    fun iniateAdapter() {
        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())
        recyclerview.adapter = ReplyMessageAdapter(list, this
        ) { item -> doClick(item) }


    }
    private fun initEditText() {
        realm.executeTransactionAsync({
            val message = it.createObject(SaveCustomeMessage::class.java)
            message.expectedMessage = expected_mesg.text.toString()
            message.replyMessage = reply_message.text.toString()

        }, {
            Log.d("Save Success", "On Success: Data Written Successfully!")
            readData()
            clearEditText()
        }, {
            Log.d("Save Success", "On Error: Error in saving Data!")
        })
    }

    private fun clearEditText() {
        expected_mesg.setText("")
        reply_message.setText("")
    }

    private fun readData() {
        list.clear()
        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())
        recyclerview.adapter = ReplyMessageAdapter(list, this
        ) { item -> doClick(item) }


    }

    fun doClick(item :SaveCustomeMessage) {
        val msgs = realm
                .where(SaveCustomeMessage::class.java)
                .findAll()

        val userdatabase = msgs
                .where()
                .equalTo("expectedMessage", item.expectedMessage)
                .equalTo("replyMessage",item.replyMessage)
                .findFirst()

        if (userdatabase != null) {

            if (!realm.isInTransaction) {
                realm.beginTransaction()
            }

            userdatabase!!.deleteFromRealm()

            realm.commitTransaction()
        }
        iniateAdapter()
     }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}