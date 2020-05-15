package com.talk2us.ui.chat.dataSource

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

import com.google.firebase.database.FirebaseDatabase
import com.talk2us.models.Message
import com.talk2us.ui.chat.ChatViewModel


class ChatRepository(private val chatDao: ChatDao, private val viewModel: ChatViewModel) {

    val allWords: LiveData<List<Message>> = chatDao.getAllMessages()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun sendMessage(message: Message) {
        chatDao.sendMessage(message)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        myRef.setValue(message.word).addOnSuccessListener {
            viewModel.update(message)
        }
    }

    @WorkerThread
    fun update(message: Message){
        chatDao.updateLast(message)
    }
    @WorkerThread
    fun deleteAll() {
        chatDao.deleteAll()
    }
}