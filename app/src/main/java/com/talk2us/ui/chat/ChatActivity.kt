package com.talk2us.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.talk2us.R

class ChatActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var viewModel: ChatViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        viewModel=ViewModelProviders.of(this).get(ChatViewModel::class.java)
        val navigationView=findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.container,ChatFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel.deleteAll()
        return true
    }
}
