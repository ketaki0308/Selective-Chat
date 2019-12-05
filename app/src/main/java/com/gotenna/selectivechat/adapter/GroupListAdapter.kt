package com.gotenna.selectivechat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gotenna.selectivechat.R
import com.gotenna.selectivechat.model.ChatGroup
import com.gotenna.selectivechat.model.Group

class GroupListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val groupList = mutableListOf<ChatGroup>()

    private val TYPE_OTHERS = 0
    private val TYPE_SELF = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cell_chat_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupListViewHolder) {
            holder.apply {
                tv_message.text = groupList[position].name
//                tv_sender.text = groupList[position].members.
                tv_sender.text = groupList[position].members?.joinToString(separator = ",",transform = {
                    it.name as CharSequence
                })
            }
        }
    }

    fun addNewGroup(chatGroup: ChatGroup){
        groupList.add(chatGroup)
        notifyItemInserted(groupList.size-1)
    }
}

class GroupListViewHolder(val chatView: View) : RecyclerView.ViewHolder(chatView) {
    val tv_message: TextView
    val tv_sender: TextView

    init {
        chatView.apply {
            tv_message = findViewById(R.id.tv_group_name1)
            tv_sender = findViewById(R.id.tv_member_name1)
        }
    }
}
