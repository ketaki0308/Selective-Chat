package com.gotenna.selectivechat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gotenna.selectivechat.R
import com.gotenna.selectivechat.adapter.GroupListAdapter

import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.gotenna.selectivechat.model.ChatGroup
import com.gotenna.selectivechat.model.Member

/**
 * Created on 12/05/2019 Thu
 *
 * @author Chuliang
 */
class GroupListFragment : Fragment() {

    lateinit var rv_chat: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_list, container, false).apply {
            rv_chat = findViewById(R.id.chatRecyclerView)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_chat.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = GroupListAdapter()
        }
        FirebaseDatabase.getInstance().reference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatGroup = ChatGroup()
                val memberList = mutableListOf<Member>()
                chatGroup.name = p0.key
                chatGroup.members = memberList
                for(member in p0.child("members").children){
                    member.getValue(Member::class.java)?.let {
                        memberList.add(it)
                    }
                }
                (rv_chat.adapter as? GroupListAdapter)?.let {
                    it.addNewGroup(chatGroup)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}