package com.talk2us.ui.session

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.talk2us.models.Counsellor
import com.talk2us.utils.Constants
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils

class SessionEndViewModel(application: Application) : AndroidViewModel(application) {
    var transaction= MutableLiveData<Fragment>()
    init {
        transaction.value=SessionEndFragment()
    }

    fun endSession(listener:FirebaseUtils.FirebaseStateListener<Counsellor>){
        FirebaseDatabase.getInstance().getReference("/session/").child(PrefManager.getChatId()).removeValue().addOnCompleteListener(
            OnCompleteListener {
                listener.onSuccess(Counsellor())
            })
    }

}
