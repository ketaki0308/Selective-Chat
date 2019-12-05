package com.gotenna.selectivechat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.gotenna.selectivechat.fragments.GroupChatFragment
import com.gotenna.selectivechat.fragments.GroupListFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPreferences(Context.MODE_PRIVATE).getString("user","").let {
            if(it.isNullOrEmpty()) {
                launchFragment(LoginFragment(),false)
            }  else {
                userName = it
                launchFragment(GroupListFragment(),false)
            }
        }

        setUpFirebase()
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

    fun launchFragment(fragment:Fragment,addToBackStack:Boolean){
        supportFragmentManager.beginTransaction().apply {
            if (addToBackStack){
                replace(R.id.frameLayout, fragment).addToBackStack(null)
            }else{
                replace(R.id.frameLayout, fragment)
            }
            commit()
        }
    }
}
