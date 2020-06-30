package com.talk2us.ui.session

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.DatabaseError
import com.talk2us.models.Counsellor
import com.talk2us.utils.Constants
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.PrefManager

class SessionEndViewModel(application: Application) : AndroidViewModel(application) {
    var transaction= MutableLiveData<Fragment>()
    init {
        transaction.value=SessionEndFragment()
    }

    fun endSession(listener:FirebaseUtils.FirebaseStateListener<Counsellor>){
        FirebaseUtils.getInstance().setCounsellorId(Counsellor(),listener)
    }

}
