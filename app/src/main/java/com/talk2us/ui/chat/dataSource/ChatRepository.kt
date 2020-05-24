package com.talk2us.ui.chat.dataSource

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.google.firebase.database.*
import com.talk2us.R
import com.talk2us.models.Counsellor
import com.talk2us.models.Message
import com.talk2us.ui.chat.ChatViewModel
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

        val database = FirebaseDatabase.getInstance()

        if (PrefManager.getString(R.string.counsellor_id, "Not_defined") == "Not_defined") {
            viewModel.progress.postValue(true)
            mDatabaseReference = FirebaseDatabase.getInstance().reference.child("Counsellor")

            mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val counsellor: Counsellor? = postSnapshot.getValue(Counsellor::class.java)
                        counsellors.add(counsellor)
                    }
                    var counsellor = counsellors[0]
                    loop@ for (i in Utils.sortList(counsellors)) {
                        if (i != null && i.available) {
                            counsellor = i
                            break@loop
                        }
                    }
                    PrefManager.putString(R.string.counsellor_id, counsellor!!.id)
                    database.getReference("counsellorChats").child(counsellor!!.id).child(
                        counsellor!!.id + PrefManager.getString(
                            R.string.client_id,
                            "Not_defined"
                        )
                    )
                        .setValue(
                            1
                        )
                        .addOnSuccessListener {
                            PrefManager.putBoolean(R.string.chat_stablished, true)
                            viewModel.update(message)
                            viewModel.progress.postValue(false)
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Utils.toast("Error finding counsellor, check your internet connection")
                    viewModel.progress.postValue(false)
                }
            })


        } else {
            if (!PrefManager.getBoolean(R.string.chat_stablished, false)) {
                database.getReference("counsellorChats")
                    .child(PrefManager.getString(R.string.counsellor_id, "not_defined") as String)
                    .child(
                        PrefManager.getString(
                            R.string.counsellor_id,
                            "Not_defined"
                        ) + PrefManager.getString(
                            R.string.client_id,
                            "Not_defined"
                        )
                    ).setValue(1)
                    .addOnSuccessListener {
                        PrefManager.putBoolean(R.string.chat_stablished, true)
                        viewModel.progress.postValue(false)
                    }
            }
            val myRef = database.getReference("chatMessages").child(
                PrefManager.getString(
                    R.string.counsellor_id,
                    "Not_defined"
                ) as String + PrefManager.getString(R.string.client_id, "Not defined")
            ).child(message.timestamp)
            myRef.setValue(message).addOnSuccessListener {
                viewModel.update(message)
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