package com.talk2us.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseError
import com.google.firebase.iid.FirebaseInstanceId
import com.talk2us.R
import com.talk2us.models.Counsellor
import com.talk2us.ui.session.SessionEndActivity
import com.talk2us.utils.Constants
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.PrefManager


class ChatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,FirebaseUtils.FirebaseStateListener<Counsellor> {
    private lateinit var viewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        sentUnsentMessages()
        FirebaseUtils.getInstance().setListeners()
        if(PrefManager.getString(
                Constants.CLIENT_MESSAGE_TOKEN,
                Constants.NOT_DEFINED
            ) == Constants.NOT_DEFINED
        ){
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
                if (!it.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = it.result?.token
                PrefManager.putClientMessageToken(token as String)
            })
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, ChatFragment()).commit()
    }

    private fun sentUnsentMessages() {
        val size = viewModel.allWords.value?.size
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
        if(item.itemId==R.id.endSession){
            startActivity(Intent(applicationContext, SessionEndActivity::class.java))
        }
        return true
    }

    override fun onSuccess(counsellor: Counsellor) {
    }

    override fun onError(e: DatabaseError?) {
        TODO("Not yet implemented")
    }
}
