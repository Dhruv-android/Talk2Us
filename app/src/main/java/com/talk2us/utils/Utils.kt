package com.talk2us.utils

import android.widget.Toast
import com.talk2us.MainApplication
import com.talk2us.models.Counsellor
import java.sql.Timestamp
import java.util.*

class Utils {
    companion object {
        fun toast(str: String) {
            Toast.makeText(MainApplication.instance.applicationContext, str, Toast.LENGTH_SHORT)
                .show()
        }

        fun sortList(list: List<Counsellor?>): List<Counsellor?> {
            val comparator = compareBy<Counsellor?> { it?.clients }
            return list.sortedWith(comparator)
        }

        fun getTime():String{
            val date=Calendar.getInstance().time
            return date.toString()
        }

        fun Log(str:String){
            android.util.Log.d("hello",str)
        }
    }
}
