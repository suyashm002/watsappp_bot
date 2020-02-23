package com.autoai.readnotification.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.autoai.readnotification.R

import com.autoai.readnotification.models.SaveCustomeMessage
import io.realm.Realm
import kotlinx.android.synthetic.main.customize_message_activity.*

import com.autoai.readnotification.ReplyMessageAdapter


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

        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())

        recyclerview.layoutManager = LinearLayoutManager(this)

       recyclerview.adapter = ReplyMessageAdapter(list, this)


        recyclerview.adapter?.notifyDataSetChanged()
        submit_button.setOnClickListener { View -> initEditText() }
    }

    private fun initEditText() {
        realm.executeTransactionAsync({
            val message = it.createObject(SaveCustomeMessage::class.java)
            message.expectedMessage = expected_mesg.text.toString()
            message.replyMessage = reply_message.text.toString()
        }, {
            Log.d("Save Success", "On Success: Data Written Successfully!")
            readData()
        }, {
            Log.d("Save Success", "On Error: Error in saving Data!")
        })
    }

    private fun readData() {

        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())

        recyclerview.adapter?.notifyDataSetChanged()

    }

}