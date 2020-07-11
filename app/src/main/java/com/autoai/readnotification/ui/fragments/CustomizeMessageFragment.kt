package com.autoai.readnotification.ui.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.autoai.readnotification.R
import com.autoai.readnotification.ReplyMessageAdapter
import com.autoai.readnotification.models.SaveCustomeMessage
import com.autoai.readnotification.ui.AboutPopup
import com.autoai.readnotification.ui.CreateUpdateActivity
import com.autoai.readnotification.ui.CustomizeMessageActivity
import com.autoai.readnotification.ui.NotificationSettingsActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.customize_message_activity.*

class CustomizeMessageFragment : Fragment() {

    private lateinit var realm: Realm
    lateinit var list: ArrayList<SaveCustomeMessage>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.customize_message_activity, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        realm.commitTransaction()



        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        iniateAdapter()
        checkIfListEmpty()
        // submit_button.setOnClickListener { initEditText() }
        notification_layout.setOnClickListener {
            openNotificationAccess()
        }
        give_permission_layout.setOnClickListener {
            openNotificationAccess()
        }
        create_msg.setOnClickListener {
            val intent = Intent(requireContext(), CreateUpdateActivity::class.java)
            intent.putExtra(CustomizeMessageActivity.IS_UPDATE_MESSAGE, false)
            startActivityForResult(intent, CustomizeMessageActivity.REQUEST_CODE_DATA)
        }

        info_button.setOnClickListener {
            val intent1 = Intent(requireContext(), AboutPopup::class.java)
            startActivity(intent1)
        }

        notification_settings.setOnClickListener {
            val intent2 = Intent(requireContext(), NotificationSettingsActivity::class.java)
            startActivity(intent2)
        }
        checkIfPermisionGiven()
    }

    private fun openNotificationAccess() {
        //Open listener reference message // Notification access
        val intent_s = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        startActivityForResult(intent_s, 100)
    }

    private fun checkIfPermisionGiven() {
        if (isNotificationServiceRunning()) {
            create_msg.visibility = View.VISIBLE
            botto_layout.visibility = View.GONE
            notification_layout.setCompoundDrawablesWithIntrinsicBounds(requireContext().getResources().getDrawable(R.drawable.notification_turned_on,null), null, null, null)
        } else {
            create_msg.visibility = View.GONE
            botto_layout.visibility = View.VISIBLE
             notification_layout.setCompoundDrawablesWithIntrinsicBounds( requireContext().getResources().getDrawable(R.drawable.notification_not_on ,null), null, null, null)
        }
    }

    fun iniateAdapter() {
        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())
        recyclerview.adapter = ReplyMessageAdapter(list, requireContext()
        ) { item -> doClick(item) }
    }

    private fun readData() {
        list.clear()
        list = ArrayList(realm.where(SaveCustomeMessage::class.java).findAll())

        if (!list.isEmpty()) {
            recyclerview.adapter = ReplyMessageAdapter(list, requireContext()
            ) { item -> doClick(item) }
        }
    }

    fun checkIfListEmpty() {
        if (!list.isEmpty()) {
            no_reply.visibility = View.GONE
        } else {
            no_reply.visibility = View.VISIBLE
        }
    }

    fun doClick(item: SaveCustomeMessage) {
        val intent = Intent(requireContext(), CreateUpdateActivity::class.java)
        intent.putExtra(IS_UPDATE_MESSAGE, true)
        intent.putExtra("DATA", item)
        startActivityForResult(intent, Companion.REQUEST_CODE_DATA)
    }



    private fun isNotificationServiceRunning(): Boolean {
        val map = NotificationManagerCompat.getEnabledListenerPackages(requireActivity().getApplicationContext()).filterIndexed { index, value -> value == requireContext().packageName }
        return map.size == 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        checkIfPermisionGiven()
        readData()
        checkIfListEmpty()
    }

    companion object {
        const val REQUEST_CODE_DATA = 1001
        const val IS_UPDATE_MESSAGE = "IS_UPDATE_MESSAGE"
    }
}