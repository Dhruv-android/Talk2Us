package com.talk2us.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.talk2us.R
import com.talk2us.ui.chat.ChatActivity
import com.talk2us.utils.Constants
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils
import java.util.*


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            startActivity(Intent(applicationContext, ChatActivity::class.java))
            finish()
        }
        if (PrefManager.getBoolean(Constants.FIRST_TIME, true)) {
            startActivity(Intent(applicationContext, WelcomeActivity::class.java))
            finish()
        }

        loginViewModel = ViewModelProviders.of(this)
            .get(LoginViewModel::class.java)
        loginViewModel.codeSent.observe(this, Observer {
            if (it) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ConfirmOtpFragment()).commit()
            }
        })
        loginViewModel.credential.observe(this, Observer {
            signInWithPhoneCredential(it)
        })

        supportFragmentManager.beginTransaction().replace(R.id.container, SendOtpFragment()).commit()


//        startActivityForResult(
//            AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setIsSmartLockEnabled(false)
//                .setAvailableProviders(
//                    listOf< AuthUI.IdpConfig>(
//                        AuthUI.IdpConfig.PhoneBuilder().build()
//                    )
//                )
//                .build(), 1
//        )

    }


    private fun signInWithPhoneCredential(it: PhoneAuthCredential) {
        mAuth.signInWithCredential(it).addOnCompleteListener {
            if (it.isSuccessful) {
                PrefManager.putString(Constants.PHONE_NUMBER, loginViewModel.phone)
                PrefManager.putString(Constants.CLIENT_ID,mAuth.uid as String)
                startActivity(Intent(applicationContext,ChatActivity::class.java))
                finish()
            } else {
                Utils.toast(it.exception.toString())
                if (it.exception is FirebaseAuthInvalidCredentialsException) {
                    Utils.toast("Invalid OTP")
                }
            }
        }
    }
}