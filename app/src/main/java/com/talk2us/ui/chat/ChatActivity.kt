package com.talk2us.ui.chat

import android.content.Intent
import android.os.Bundle
 import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.iid.FirebaseInstanceId
import com.talk2us.R
import com.talk2us.models.Counsellor
import com.talk2us.ui.login.LoginActivity
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.Utils


class ChatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,FirebaseUtils.FirebaseStateListener<Counsellor> {
    private lateinit var viewModel: ChatViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser == null) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        sentUnsentMessages()
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
            val token=it.result?.token as String
            Utils.log("YO")
            Utils.log(token)
        })
        supportFragmentManager.beginTransaction().replace(R.id.container, ChatFragment()).commit()
    }

    private fun sentUnsentMessages() {
        var size = viewModel.allWords.value?.size
        var i = size?.minus(1)
        while (i != null && i > 0) {
            if (!viewModel.allWords.value!![i].sent) {
                viewModel.sendMessage(viewModel.allWords.value!![i])
                i -= 1
            } else {
                break
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.deleteAll()
        return true
    }

    override fun onSuccess(counsellor: Counsellor) {
            Utils.log("hello"+counsellor.getId())
    }

    override fun onError(e: DatabaseError?) {
        TODO("Not yet implemented")
    }
}
