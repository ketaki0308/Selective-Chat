package com.gotenna.selectivechat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.gotenna.selectivechat.R
import com.gotenna.selectivechat.adapter.ChatAdapter
import com.gotenna.selectivechat.model.ChatGroup
import com.gotenna.selectivechat.model.Member
import com.gotenna.selectivechat.model.Message
import com.gotenna.selectivechat.userName
import kotlinx.android.synthetic.main.chat_fragment.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created on 12/04/2019 Wed
 *
 * @author Chuliang
 */
class GroupChatFragment : Fragment() {
    lateinit var rv_chat:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_fragment,container,false).apply {
            rv_chat = findViewById(R.id.chatRecyclerView1)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_chat.apply {
            layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
            adapter = ChatAdapter()
        }
        val groupName = arguments?.getParcelable<ChatGroup>(KEY_CHAT_GROUP)?.name
        tv_group_name.text = groupName
        arguments?.getParcelable<ChatGroup>(KEY_CHAT_GROUP)?.members?.joinToString(separator = ",",transform = {
            it.name as CharSequence
        })?.let {
            tv_member_name.text = it
        }

        val database = FirebaseDatabase.getInstance().reference.child(groupName?:"Hack_day").child("messages")

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val messageList = mutableListOf<Message>()
                for(message in p0.children){
                    messageList.add(message.getValue(Message::class.java) as Message)
                }
                (rv_chat.adapter as? ChatAdapter?)?.let{chatAdapter ->
                    chatAdapter.updateMessageList(messageList)
                }
                scrollToBottom()
//                addFirebaseDataChangeListener(database)
            }
        })

        iv_send.setOnClickListener {
            database.child(System.currentTimeMillis().toString()).setValue(Message().apply {
                text = et_message.text.toString()
                sender = userName
                timeStamp = System.currentTimeMillis().toString()
            })
            et_message.text.clear()
            scrollToBottom()
        }
    }

    private fun addFirebaseDataChangeListener(database: DatabaseReference) {
        database.let {
            it.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val message = p0.getValue(Message::class.java)
                    (rv_chat.adapter as? ChatAdapter?)?.let { chatAdapter ->
                        message?.let { message ->
                            chatAdapter.addNewMessage(message)
                        }
                    }
                    scrollToBottom()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        scrollToBottom()
    }

    fun scrollToBottom(){
        rv_chat.post {
            rv_chat.smoothScrollToPosition(chatRecyclerView1.adapter?.itemCount?:0)
        }
    }

    companion object{
        const val KEY_CHAT_GROUP = "KEY_CHAT_GROUP"

        fun create(chatGroup:ChatGroup):GroupChatFragment{
            return GroupChatFragment().apply {
                arguments = Bundle().also {
                    it.putParcelable(KEY_CHAT_GROUP,chatGroup)
                }
            }
        }
    }

}