package com.gotenna.selectivechat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.gotenna.selectivechat.fragments.GroupChatFragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId


class MainActivity : AppCompatActivity() {

    private val userMutableList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUI()
        setUpFirebase()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, GroupChatFragment())
            commit()
        }
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

    private fun setUI() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, getUserList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        userSpinner.adapter = adapter
//        userSpinner.setSelection(0)
//        userSpinner.onItemSelectedListener = this
    }

       if(getPreferences(Context.MODE_PRIVATE).getString("user","").isNullOrEmpty()) {
           supportFragmentManager.beginTransaction().apply {
               replace(R.id.frameLayout, LoginFragment())
               commit()
           }
       }  else {

           //TODO : Go To Chat
           supportFragmentManager.beginTransaction().apply {
               replace(R.id.frameLayout, LoginFragment())
               commit()
           }
       }
        setUpFirebase()
    }



    fun setUpFirebase() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MyFirebaseMsgService", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                Log.d("TAG", token)
            })

    }

override fun onDestroy() {
    super.onDestroy()
    userMutableList.clear()
}
}
