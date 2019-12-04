package com.gotenna.selectivechat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setUI()

        setUpFirebase()
    }

    private fun getUserList(): List<String> {

        val database = FirebaseDatabase.getInstance().reference

        val userMutableList = mutableListOf<String>()
        userMutableList.add(0,"Select User")

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
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, getUserList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.adapter = adapter
        user_spinner.setSelection(0)
        user_spinner.onItemSelectedListener = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //user_spinner.prompt = "Please select user from list"
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("user", getUserList()[p2])
            commit()
        }

        //go to your chat fragment
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
}
