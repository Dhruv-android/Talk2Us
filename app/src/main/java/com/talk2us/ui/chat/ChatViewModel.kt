package com.talk2us.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.talk2us.models.Message
import com.talk2us.ui.chat.dataSource.ChatDatabase
import com.talk2us.ui.chat.dataSource.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChatRepository
    val allWords: LiveData<List<Message>>

    init {
        val wordsDao = ChatDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = ChatRepository(wordsDao, this)
        allWords = repository.allWords
    }

    fun sendMessage(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        repository.sendMessage(message)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun update(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        message.sent = true
        repository.update(message)
    }
}
