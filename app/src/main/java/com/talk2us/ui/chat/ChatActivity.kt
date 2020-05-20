package com.talk2us.ui.chat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase
import com.talk2us.R
import com.talk2us.models.Client
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils


class ChatActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewModel: ChatViewModel
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        sentUnsentMessages()
        supportFragmentManager.beginTransaction().replace(R.id.container, ChatFragment()).commit()
    }
    private fun sentUnsentMessages(){
        var size=viewModel.allWords.value?.size
        var i=size?.minus(1)
        while(i!=null && i>0){
            if(!viewModel.allWords.value!![i].sent){
                viewModel.sendMessage(viewModel.allWords.value!![i])
                i -= 1
            }
            else{
                break
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.deleteAll()
        return true
    }
}
