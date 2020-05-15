package com.talk2us.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.talk2us.R
import com.talk2us.ui.login.LoginViewModel

class SendOtpFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loading: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_send_otp, container, false)
        val login = v.findViewById<Button>(R.id.sendOtp)
        val phone = v.findViewById<EditText>(R.id.phone)
        loading=v.findViewById(R.id.loading)
        login.setOnClickListener {
            loginViewModel.sendCode(phone.text.toString())
        }

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel=ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)
        loginViewModel.progress.observe(viewLifecycleOwner, Observer {
            if(it){
                loading.visibility=View.VISIBLE
            }
            else{
                loading.visibility=View.INVISIBLE
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendOtpFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}