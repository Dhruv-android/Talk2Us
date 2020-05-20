package com.talk2us.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.talk2us.utils.Utils
import java.util.concurrent.TimeUnit

class LoginViewModel : ViewModel() {
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    public var phone:String=""
    private val _codeSent = MutableLiveData<Boolean>()
    val codeSent: LiveData<Boolean> = _codeSent

    public val _credential = MutableLiveData<PhoneAuthCredential>()
    val credential: LiveData<PhoneAuthCredential> = _credential

    public var mVerificationID = ""

    fun sendCode(phone: String) {
        this.phone=phone
        _progress.postValue(true)
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$phone",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks
        )
    }

    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _progress.postValue(false)
                _credential.postValue(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _progress.postValue(false)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Utils.toast("Invalid OTP")
                } else if (e is FirebaseTooManyRequestsException) {
                    Utils.toast("Too many requests")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: ForceResendingToken
            ) {
                mVerificationID = verificationId
                _progress.postValue(false)
                _codeSent.postValue(true)
            }
        }

}
