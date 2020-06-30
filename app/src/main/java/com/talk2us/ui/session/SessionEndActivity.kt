package com.talk2us.ui.session

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.talk2us.R

class SessionEndActivity : AppCompatActivity() {
    lateinit var viewModel: SessionEndViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_end)
        viewModel=ViewModelProviders.of(this).get(SessionEndViewModel::class.java)
        viewModel.transaction.observe(this, Observer {
            supportFragmentManager.beginTransaction().replace(R.id.container,it).commit()
        })
    }
}
