package com.autoai.readnotification

import android.content.Context
 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.autoai.readnotification.models.SaveCustomeMessage
import kotlinx.android.synthetic.main.item_message.view.*

class ReplyMessageAdapter(private val items : ArrayList<SaveCustomeMessage>, val context : Context,val listner : (SaveCustomeMessage)-> Unit ) : RecyclerView.Adapter<ReplyMessageAdapter.ViewHolder>() {

    private var listener: ((item: SaveCustomeMessage) -> Unit)? = null

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.setItem(items.get(p1))
       // holder?.messageExpectedText?.text = items[p1].expectedMessage
        holder?.replyMessageText?.text = items[p1].replyMessage

     }

    override fun getItemCount(): Int {
        return items.size
     }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message,p0,false))
     }
   inner class ViewHolder (view : View) : RecyclerView.ViewHolder(view) {
        val messageExpectedText = view.expected_msg_txt
        val replyMessageText = view.reply_msg_txt
        val messageExpectedHeader = view.expected_msg_txt_header
        val messageReplyHeader = view.reply_msg_txt_header
        val msgLayout = view.message_layout
       fun setItem(item: SaveCustomeMessage) {
           messageExpectedText?.text = item.expectedMessage
           replyMessageText?.text = item.replyMessage
           msgLayout.setOnClickListener {
               listner(item) }
       }


    }

 }