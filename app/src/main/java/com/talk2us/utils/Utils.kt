package com.talk2us.utils

import android.widget.Toast
import com.talk2us.MainApplication
import java.sql.Timestamp

class Utils {
    companion object{
        fun toast(str:String){
            Toast.makeText(MainApplication.instance.applicationContext,str,Toast.LENGTH_SHORT).show()
        }
        fun getCurrentTime():Timestamp{
            return Timestamp(232)
        }
    }
}