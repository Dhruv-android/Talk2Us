package com.talk2us.ui.account

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.talk2us.utils.Constants
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils

class AccountViewModel: ViewModel() {
    public val change=MutableLiveData<Boolean>()
    public val name= MutableLiveData<String>()

    fun onClick(v:View){
        Utils.toast("YOU")
    }

}