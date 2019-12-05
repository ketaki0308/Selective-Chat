package com.gotenna.selectivechat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gotenna.selectivechat.fragments.GroupChatFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val userMutableList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUI()
    }

    private fun setUI() {
        val adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item, getUserList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.adapter = adapter
        userSpinner.setSelection(0)
        userSpinner.onItemSelectedListener = this
    }

    private fun getUserList(): List<String> {

        val database = FirebaseDatabase.getInstance().reference

        userMutableList.add(0, "Select")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (a in dataSnapshot.children) {
                    userMutableList.add(a.child("name").value.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.child("Hack_day").child("members").addValueEventListener(postListener)

        return userMutableList
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
        //user_spinner.prompt = "Please select user from list"
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2 > 0){
            val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString("user", userMutableList[p2])
                commit()
            }

            (activity as? MainActivity)?.run {
                launchFragment(GroupChatFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userMutableList.clear()
    }
}