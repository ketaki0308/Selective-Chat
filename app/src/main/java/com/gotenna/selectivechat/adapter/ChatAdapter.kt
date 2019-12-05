package com.gotenna.selectivechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gotenna.selectivechat.R
import com.gotenna.selectivechat.model.Message
import com.gotenna.selectivechat.userName

/**
 * Created on 12/04/2019 Wed
 *
 * @author Chuliang
 */
class ChatAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    private val userName:String? = com.gotenna.selectivechat.userName
    private val messageList= mutableListOf<Message>()

    private val TYPE_OTHERS = 0
    private val TYPE_SELF = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_SELF){
            return SelfChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_chat_self,parent,false))
        }else{
            return OthersChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_chat_others,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OthersChatViewHolder){
            holder.apply {
                tv_message.text = messageList[position].text
                if (position == messageList.size-1){
                    tv_sender.visibility = View.VISIBLE
                    tv_sender.text = messageList[position].sender
                }else if(messageList[position+1].sender.equals(messageList[position].sender)){
                    tv_sender.visibility = View.GONE
                }else{
                    tv_sender.visibility = View.VISIBLE
                    tv_sender.text = messageList[position].sender
                }
            }
        }else if(holder is SelfChatViewHolder){
            holder.apply {
                tv_message.text = messageList[position].text
                if (position == messageList.size-1){
                    tv_sender.visibility = View.VISIBLE
                    tv_sender.text = "Me"
                }else if(messageList[position+1].sender.equals(messageList[position].sender)){
                    tv_sender.visibility = View.GONE
                }else{
                    tv_sender.visibility = View.VISIBLE
                    tv_sender.text = "Me"
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (messageList[position].sender.equals(userName)){
            return TYPE_SELF
        }else{
            return TYPE_OTHERS
        }
    }

    fun addNewMessage(message: Message){
       messageList.add(message)
//       notifyItemInserted(messageList.size-1)
        notifyDataSetChanged()
    }
}

class OthersChatViewHolder(val chatView:View):RecyclerView.ViewHolder(chatView){
    val tv_message:TextView
    val tv_sender:TextView
    init {
        chatView.apply {
            tv_message = findViewById(R.id.tv_message)
            tv_sender = findViewById(R.id.tv_additonal)
        }
    }
}

class SelfChatViewHolder(val chatView: View):RecyclerView.ViewHolder(chatView){
    val tv_message:TextView
    val tv_sender:TextView
    init {
        chatView.apply {
            tv_message = findViewById(R.id.tv_message)
            tv_sender = findViewById(R.id.tv_additonal)
        }
    }
}