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
import com.gotenna.selectivechat.model.Member
import com.gotenna.selectivechat.model.Message
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
        tv_group_name.text = "Hack Day Demo"
        var membersBuilder = StringBuilder()
        FirebaseDatabase.getInstance().reference.child("Hack_day").child("members").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val member = p0.getValue(Member::class.java)
                member?.name?.let {
                    membersBuilder.append("${it},")
                    tv_member_name.text = membersBuilder.substring(0,membersBuilder.length-1).toString()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })

        FirebaseDatabase.getInstance().reference.child("Hack_day").child("messages").let {datebase->
            datebase.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val message =  p0.getValue(Message::class.java)
                    (rv_chat.adapter as? ChatAdapter?)?.let{chatAdapter ->
                        message?.let {message->
                            chatAdapter.addNewMessage(message)
                        }
                    }
                    scrollToBottom()
                }
                override fun onChildRemoved(p0: DataSnapshot) {
                }
            })

            iv_send.setOnClickListener {
                datebase.child(System.currentTimeMillis().toString()).setValue(Message().apply {
                    text = et_message.text.toString()
                    sender = "Chuliang"
                    timeStamp = SimpleDateFormat("yyyy/MM/dd HH.mm.ss").format(Date())
                })
                et_message.text.clear()
                scrollToBottom()
            }
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

}