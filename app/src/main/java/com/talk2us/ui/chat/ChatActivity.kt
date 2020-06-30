package com.talk2us.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseError
import com.talk2us.R
import com.talk2us.models.Counsellor
import com.talk2us.ui.session.SessionEndActivity
import com.talk2us.utils.FirebaseUtils


class ChatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,FirebaseUtils.FirebaseStateListener<Counsellor> {
    private lateinit var viewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        sentUnsentMessages()
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
