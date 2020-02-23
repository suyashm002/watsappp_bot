package com.autoai.readnotification

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.autoai.readnotification.models.SaveCustomeMessage
import kotlinx.android.synthetic.main.item_message.view.*

class ReplyMessageAdapter(private val items : ArrayList<SaveCustomeMessage>, val context : Context) : RecyclerView.Adapter<ReplyMessageAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder?.messageExpectedText?.text = items[p1].expectedMessage
        holder?.replyMessageText?.text = items[p1].replyMessage
     }

    override fun getItemCount(): Int {
        return items.size
     }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message,p0,false))
     }
    class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val messageExpectedText = view.expected_msg_txt
        val replyMessageText = view.reply_msg_txt
        val messageExpectedHeader = view.expected_msg_txt_header
        val messageReplyHeader = view.reply_msg_txt_header
    }
}