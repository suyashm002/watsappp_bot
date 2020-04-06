package com.suyash.autoreply.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
 import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.suyash.autoreply.R
import com.suyash.autoreply.ReplyMessageAdapter
import com.suyash.autoreply.models.SaveCustomeMessage
import io.realm.Realm
import kotlinx.android.synthetic.main.customize_message_activity.*


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
       // submit_button.setOnClickListener { initEditText() }
        notification_layout.setOnClickListener {
            //Open listener reference message // Notification access
        val intent_s = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
            startActivity(intent_s) }

        create_msg.setOnClickListener{
            val intent = Intent(this, CreateUpdateActivity::class.java)
            startActivityForResult(intent, Companion.REQUEST_CODE_DATA)
        }
    }

    fun iniateAdapter() {

        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())
        if(!list.isEmpty()) {
            no_reply.visibility = View.GONE
            recyclerview.adapter = ReplyMessageAdapter(list, this
            ) { item -> doClick(item) }
        } else {
            no_reply.visibility = View.VISIBLE
        }


    }

    /*private fun initEditText() {
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
*/
  /*  private fun clearEditText() {
        expected_mesg.setText("")
        reply_message.setText("")
    }
*/
    private fun readData() {
        list.clear()
        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())
        recyclerview.adapter = ReplyMessageAdapter(list, this
        ) { item -> doClick(item) }


    }

    fun doClick(item: SaveCustomeMessage) {
        val msgs = realm
                .where(SaveCustomeMessage::class.java)
                .findAll()

        val userdatabase = msgs
                .where()
                .equalTo("expectedMessage", item.expectedMessage)
                .equalTo("replyMessage", item.replyMessage)
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
        finish()
        super.onBackPressed()
    }

    private fun isNotificationServiceRunning(): Boolean {
        val contentResolver = contentResolver
        val enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName = packageName
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        readData()
    }

    companion object {
        const val  REQUEST_CODE_DATA = 1001
    }
}