package com.talk2us.ui.chat.dataSource

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.talk2us.models.Counsellor
import com.talk2us.models.Message
import com.talk2us.ui.chat.ChatViewModel
import com.talk2us.utils.Constants.CHAT_ESTABLISHED
import com.talk2us.utils.Constants.COUNSELLOR_ID
import com.talk2us.utils.Constants.NOT_DEFINED
import com.talk2us.utils.FirebaseUtils
import com.talk2us.utils.PrefManager
import com.talk2us.utils.Utils


class ChatRepository(private val chatDao: ChatDao, private val viewModel: ChatViewModel) {

    val allWords: LiveData<List<Message>> = chatDao.getAllMessages()
    lateinit var mDatabaseReference: DatabaseReference
    var counsellors = ArrayList<Counsellor?>()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun sendMessage(message: Message) {
        chatDao.sendMessage(message)
        if (PrefManager.getString(COUNSELLOR_ID, NOT_DEFINED) == NOT_DEFINED) {
            viewModel.progress.postValue(true)
            FirebaseUtils.getInstance()
                .getSuitableCounsellor(object : FirebaseUtils.FirebaseStateListener<Counsellor> {
                    override fun onSuccess(counsellor: Counsellor?) {
                        PrefManager.putString(COUNSELLOR_ID, counsellor!!.id)
                        FirebaseUtils.getInstance()
                            .establishChat(object : FirebaseUtils.FirebaseStateListener<Boolean> {
                                override fun onSuccess(counsellor: Boolean?) {
                                    PrefManager.putBoolean(CHAT_ESTABLISHED, true)
                                    message.messageId = PrefManager.getChatId()
                                    viewModel.progress.postValue(false)
                                    viewModel.update(message)
                                }

                                override fun onError(e: DatabaseError?) {
                        }

                            })
                }

                    override fun onError(e: DatabaseError?) {
                }

            })


        } else {
            if (!PrefManager.getBoolean(CHAT_ESTABLISHED, false)) {
                FirebaseUtils.getInstance()
                    .establishChat(object : FirebaseUtils.FirebaseStateListener<Boolean> {
                        override fun onSuccess(counsellor: Boolean?) {
                            PrefManager.putBoolean(CHAT_ESTABLISHED, counsellor as Boolean)
                        viewModel.progress.postValue(false)
                    }

                        override fun onError(e: DatabaseError?) {
                        }

                    })
            } else {
                FirebaseUtils.getInstance()
                    .sendMessage(message, object : FirebaseUtils.FirebaseStateListener<Message> {
                        override fun onSuccess(counsellor: Message?) {
                            viewModel.update(counsellor as Message)
                        }
                        override fun onError(e: DatabaseError?) {
                            Utils.toast("Something went wrong")
                        }

                    })
            }
        }
    }

    @WorkerThread
    fun update(message: Message) {
        chatDao.updateLast(message)
    }

    @WorkerThread
    fun insertLocally(message: Message) {
        chatDao.sendMessage(message)
    }

    @WorkerThread
    fun deleteAll() {
        chatDao.deleteAll()
    }
}