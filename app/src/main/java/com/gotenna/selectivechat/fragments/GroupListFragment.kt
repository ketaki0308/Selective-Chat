package com.gotenna.selectivechat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gotenna.selectivechat.R
import com.gotenna.selectivechat.adapter.ChatAdapter
import com.gotenna.selectivechat.adapter.GroupListAdapter

class GroupListFragment : Fragment() {
    lateinit var rv_chat: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_list,container,false).apply {
            rv_chat = findViewById(R.id.chatRecyclerView)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        rv_chat.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = GroupListAdapter()
        }
    }

}